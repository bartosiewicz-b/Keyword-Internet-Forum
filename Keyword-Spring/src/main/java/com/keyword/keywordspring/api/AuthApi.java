package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.EmailAlreadyTakenException;
import com.keyword.keywordspring.exception.InvalidUsernameOrPasswordException;
import com.keyword.keywordspring.exception.UsernameAlreadyTakenException;
import com.keyword.keywordspring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthApi {

    private final UserService userService;

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
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            userService.login(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
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
