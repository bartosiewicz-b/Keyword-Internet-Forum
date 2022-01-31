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

        AppUser user = userService.login(request);

        return ResponseEntity.ok().body(jwtUtil.generateTokenResponse(user));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody String refresh) {

        return ResponseEntity.ok().body(jwtUtil.refreshJwt(refresh));
    }

    @PostMapping("/change-username")
    public ResponseEntity<TokenResponse> changeUsername(@RequestHeader("Authorization") String token,
                                                        @RequestBody String newUsername) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        userService.changeUsername(token, newUsername);

        return ResponseEntity.ok().body(jwtUtil.generateTokenResponse(user));
    }

    @PostMapping("/change-email")
    public ResponseEntity<TokenResponse> changeEmail(@RequestHeader("Authorization") String token,
                                                     @RequestBody ChangeEmailRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        userService.changeEmail(token, request.getPassword(), request.getNewEmail());

        return ResponseEntity.ok().body(jwtUtil.generateTokenResponse(user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<TokenResponse> changePassword(@RequestHeader("Authorization") String token,
                                                        @RequestBody ChangePasswordRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        userService.changePassword(token, request.getOldPassword(), request.getNewPassword());

        return ResponseEntity.ok().body(jwtUtil.generateTokenResponse(user));
    }

    @PostMapping("/validate-new-username")
    public ResponseEntity<Boolean> validateNewUsername(@RequestBody String username) {

        return ResponseEntity.ok().body(userService.validateNewUsername(username));
    }

    @PostMapping("/validate-new-email")
    public ResponseEntity<Boolean> validateNewEmail(@RequestBody String email) {

        return ResponseEntity.ok().body(userService.validateNewEmail(email));
    }
}
