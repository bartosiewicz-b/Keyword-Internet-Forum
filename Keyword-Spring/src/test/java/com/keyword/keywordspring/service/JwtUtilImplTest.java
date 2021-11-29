package com.keyword.keywordspring.service;

import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.InvalidTokenRepository;
import com.keyword.keywordspring.repository.UserRepository;
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
    void generateAndValidateJwt() {
        String token = jwtUtil.generateJwt(AppUser.builder().username("username").build());

        jwtUtil.validateJwt(token);
    }

    @Test
    void invalidToken() {
        String token = "invalidToken";

        assertFalse(jwtUtil.validateJwt(token));
    }

    @Test
    void getUsernameFromJwt() {
        String token = jwtUtil.generateJwt(AppUser.builder().username("username").build());

        assertEquals(jwtUtil.getUsernameFromJwt(token), "username");
    }

    @Test
    void getTypeFromJwt() {
        String token = jwtUtil.generateJwt(AppUser.builder().username("username").build());
        String refresh = jwtUtil.generateRefresh(AppUser.builder().username("username").build());

        assertEquals(jwtUtil.getTypeFromJwt(token), "token");
        assertEquals(jwtUtil.getTypeFromJwt(refresh), "refresh");
    }

    @Test
    void generateAndValidateRefresh() {
        String refresh = jwtUtil.generateRefresh(AppUser.builder().username("username").build());

        jwtUtil.validateJwt(refresh);
    }
}