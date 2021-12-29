package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.response.AuthResponse;
import com.keyword.keywordspring.model.AppUser;

import java.util.Optional;

public interface JwtUtil {

    AuthResponse generateTokenResponse(AppUser user);

    Optional<AuthResponse> refreshJwt(String refreshToken);

    boolean validateJwt(String token);

    AppUser getUserFromToken(String token);

    String getUsernameFromJwt(String token);
    String getTypeFromJwt(String token);
}
