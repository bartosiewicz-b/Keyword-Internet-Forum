package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangePasswordRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class AuthApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {

        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        AppUser user = userService.login(request)
                .orElseThrow(UnauthorizedException::new);

        TokenResponse response = jwtUtil.generateTokenResponse(user);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader("refresh") String refresh) {

        TokenResponse response = jwtUtil.refreshJwt(refresh)
                .orElseThrow(UnauthorizedException::new);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/change/username")
    public ResponseEntity<TokenResponse> changeUsername(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(
                userService.changeUsername(request.get("username"), user)
        );
    }

    @PostMapping("/change/email")
    public ResponseEntity<TokenResponse> changeEmail(@RequestHeader("Authorization") String token,
            @RequestBody ChangeEmailRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(
                userService.changeEmail(request, user)
        );
    }

    @PostMapping("/change/password")
    public ResponseEntity<TokenResponse> changePassword(@RequestHeader("Authorization") String token,
                                                     @RequestBody ChangePasswordRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(
                userService.changePassword(request, user)
        );
    }

    @PostMapping("/validate-new/username")
    public ResponseEntity<Boolean> validateNewUsername(@RequestBody Map<String, String> request) {

        if(null == request.get("username") || userService.isUsernameTaken(request.get("username")))
            return ResponseEntity.ok().body(false);
        else
            return ResponseEntity.ok().body(true);
    }

    @PostMapping("/validate-new/email")
    public ResponseEntity<Boolean> validateNewEmail(@RequestBody Map<String, String> request) {

        if(null == request.get("email") || userService.isEmailTaken(request.get("email")))
            return ResponseEntity.ok().body(false);
        else
            return ResponseEntity.ok().body(true);
    }
}
