package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.JwtDto;
import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.EmailAlreadyTakenException;
import com.keyword.keywordspring.exception.InvalidUsernameOrPasswordException;
import com.keyword.keywordspring.exception.UsernameAlreadyTakenException;
import com.keyword.keywordspring.service.UserService;
import lombok.AllArgsConstructor;
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
        } catch (UsernameAlreadyTakenException e) {
            return ResponseEntity.badRequest().body(e.toString());
        } catch (EmailAlreadyTakenException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        JwtDto jwt;

        try {
            jwt = userService.login(request);
        } catch (InvalidUsernameOrPasswordException e) {
            return ResponseEntity.status(401).body(e.toString());
        }

        return ResponseEntity.ok().body(jwt);
    }
}
