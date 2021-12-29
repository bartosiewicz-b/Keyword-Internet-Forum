package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.request.*;
import com.keyword.keywordspring.dto.response.AuthResponse;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
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

        try {
            userService.register(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        AppUser user = userService.login(request)
                .orElseThrow(UnauthorizedException::new);

        AuthResponse response = jwtUtil.generateTokenResponse(user);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("refresh") String refresh) {

        AuthResponse response = jwtUtil.refreshJwt(refresh)
                .orElseThrow(UnauthorizedException::new);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/change/username")
    public ResponseEntity<AuthResponse> changeUsername(@RequestHeader("Authorization") String token,
                                                       @RequestBody NameRequest request) {

        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(
                    userService.changeUsername(request.getName(), user)
            );
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/change/email")
    public ResponseEntity<AuthResponse> changeEmail(@RequestHeader("Authorization") String token,
                                                    @RequestBody ChangeEmailRequest request) {

        try {
            AppUser user = jwtUtil.getUserFromToken(token);
            return ResponseEntity.ok().body(
                    userService.changeEmail(request, user)
            );
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/change/password")
    public ResponseEntity<AuthResponse> changePassword(@RequestHeader("Authorization") String token,
                                                       @RequestBody ChangePasswordRequest request) {

        try {
            AppUser user = jwtUtil.getUserFromToken(token);
            return ResponseEntity.ok().body(
                    userService.changePassword(request, user)
            );
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/validate-new/username")
    public ResponseEntity<Boolean> validateNewUsername(@RequestBody NameRequest request) {

        if(null == request.getName()|| userService.isUsernameTaken(request.getName()))
            return ResponseEntity.ok().body(false);
        else
            return ResponseEntity.ok().body(true);
    }

    @PostMapping("/validate-new/email")
    public ResponseEntity<Boolean> validateNewEmail(@RequestBody EmailRequest request) {

        if(null == request.getEmail() || userService.isEmailTaken(request.getEmail()))
            return ResponseEntity.ok().body(false);
        else
            return ResponseEntity.ok().body(true);
    }
}
