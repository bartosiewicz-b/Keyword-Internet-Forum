package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.EmailAlreadyTakenException;
import com.keyword.keywordspring.exception.UsernameAlreadyTakenException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.JwtUtil;
import com.keyword.keywordspring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthApiTest {

    @Mock
    UserService userService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    AuthApi api;

    RegisterRequest registerRequest;
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder().build();
        loginRequest = LoginRequest.builder().build();
    }

    @Test
    void registerSuccessful() {

        ResponseEntity response = api.register(registerRequest);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void loginSuccessful() {

        when(userService.login(any())).thenReturn(AppUser.builder().build());
        when(jwtUtil.generateJwt(any())).thenReturn("");

        ResponseEntity response = api.login(loginRequest);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}