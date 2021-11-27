package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.LoginResponse;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.JwtUtil;
import com.keyword.keywordspring.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        AppUser user;

        try {
            user = userService.login(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(LoginResponse.builder()
                .token(jwtUtil.generateJwt(user))
                .refreshToken(jwtUtil.generateRefresh(user))
                .build());
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("refresh") String refresh) {

        try {
            return ResponseEntity.ok().body(jwtUtil.refreshJwt(refresh));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/validate-new/username")
    public ResponseEntity<String> validateNewUsername(@RequestBody String username) {

        try {
            userService.validateNewUsername(username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-new/email")
    public ResponseEntity<String> validateNewEmail(@RequestBody String email) {

        try {
            userService.validateNewEmail(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-new/password")
    public ResponseEntity<String> validateNewPassword(@RequestBody String password) {

        try {
            userService.validateNewPassword(password);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
