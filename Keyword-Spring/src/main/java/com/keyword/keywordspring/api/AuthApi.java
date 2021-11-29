package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.LoginResponse;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.JwtUtil;
import com.keyword.keywordspring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
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

        if(userService.isUsernameTaken(username))
            return ResponseEntity.badRequest().body("Username already taken.");
        else
            return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-new/email")
    public ResponseEntity<String> validateNewEmail(@RequestBody String email) {

        if(userService.isEmailTaken(email))
            return ResponseEntity.badRequest().body("Account with this email already exists.");
        else
            return ResponseEntity.ok().build();
    }
}
