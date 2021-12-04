package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.response.LoginResponse;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.InvalidTokenRepository;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    InvalidTokenRepository invalidTokenRepository;

    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtilImpl(userRepository, invalidTokenRepository);
    }

    @Test
    void generateLoginResponse() {

        LoginResponse response = jwtUtil.generateLoginResponse(AppUser.builder().username("username").build());

        jwtUtil.validateJwt(response.getToken());
    }

    @Test
    void invalidToken() {
        String token = "invalidToken";

        assertFalse(jwtUtil.validateJwt(token));
    }

    @Test
    void getUsernameFromJwt() {
        String token = jwtUtil.generateLoginResponse(AppUser.builder().username("username").build()).getToken();

        assertEquals(jwtUtil.getUsernameFromJwt(token), "username");
    }

    @Test
    void getTypeFromJwt() {
        LoginResponse response = jwtUtil.generateLoginResponse(AppUser.builder().username("username").build());

        assertEquals(jwtUtil.getTypeFromJwt(response.getToken()), "token");
        assertEquals(jwtUtil.getTypeFromJwt(response.getRefreshToken()), "refresh");
    }
}