package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangeUsernameRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.response.LoginResponse;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ReturnValue;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.UserService;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        ReturnValue<AppUser> user = userService.login(request);

        if(user.isOk())
            return ResponseEntity.ok().body(jwtUtil.generateLoginResponse(user.get()));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user.getError());
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<?> refreshToken(@RequestHeader("refresh") String refresh) {

        ReturnValue<LoginResponse> response = jwtUtil.refreshJwt(refresh);

        if(response.isOk())
            return ResponseEntity.ok().body(response.get());
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getError());
    }

    @PostMapping("/change/username")
    public ResponseEntity<?> changeUsername(@RequestBody ChangeUsernameRequest request) {

        try {
            userService.changeUsername(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change/email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest request) {

        try {
            userService.changeEmail(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate-new/username")
    public ResponseEntity<Boolean> validateNewUsername(@RequestBody String username) {

        if(userService.isUsernameTaken(username))
            return ResponseEntity.badRequest().body(false);
        else
            return ResponseEntity.ok().body(true);
    }

    @PostMapping("/validate-new/email")
    public ResponseEntity<Boolean> validateNewEmail(@RequestBody String email) {

        if(userService.isEmailTaken(email))
            return ResponseEntity.badRequest().body(false);
        else
            return ResponseEntity.ok().body(true);
    }
}
