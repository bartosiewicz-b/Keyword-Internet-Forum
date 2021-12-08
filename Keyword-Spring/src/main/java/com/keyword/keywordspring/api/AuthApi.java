package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangeUsernameRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
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
@AllArgsConstructor
public class AuthApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        AppUser user = userService.login(request)
                .orElseThrow(UnauthorizedException::new);

        return ResponseEntity.ok().body(jwtUtil.generateLoginResponse(user));
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader("refresh") String refresh) {

        TokenResponse response = jwtUtil.refreshJwt(refresh)
                .orElseThrow(UnauthorizedException::new);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/change/username")
    public ResponseEntity<Void> changeUsername(@RequestBody ChangeUsernameRequest request) {

        try {
            userService.changeUsername(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/change/email")
    public ResponseEntity<Void> changeEmail(@RequestBody ChangeEmailRequest request) {

        try {
            userService.changeEmail(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/validate-new/username")
    public ResponseEntity<Void> validateNewUsername(@RequestBody Map<String, String> request) {

        if(null == request.get("username") || userService.isUsernameTaken(request.get("username")))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-new/email")
    public ResponseEntity<Void> validateNewEmail(@RequestBody Map<String, String> request) {

        if(null == request.get("email") || userService.isEmailTaken(request.get("email")))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }
}
