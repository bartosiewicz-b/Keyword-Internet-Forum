package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.model.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilImplTest {

    @InjectMocks
    JwtUtilImpl jwtUtil;

    @Test
    void invalidToken() {
        String token = "invalidToken";

        assertFalse(jwtUtil.validateJwt(token));
    }

    @Test
    void getUsernameFromJwt() {
        TokenResponse response = jwtUtil.generateTokenResponse(AppUser.builder().username("username").build());

        assertEquals("username", jwtUtil.getUsernameFromJwt(response.getToken()));
    }

    @Test
    void getTypeFromJwt() {
        TokenResponse response = jwtUtil.generateTokenResponse(AppUser.builder().username("username").build());

        assertEquals(jwtUtil.getTypeFromJwt(response.getToken()), "token");
        assertEquals(jwtUtil.getTypeFromJwt(response.getRefreshToken()), "refresh");
    }
}