package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.model.AppUser;

import java.util.Optional;

public interface JwtUtil {

    TokenResponse generateLoginResponse(AppUser user);

    Optional<TokenResponse> refreshJwt(String refreshToken);

    boolean validateJwt(String token);

    AppUser getUserFromToken(String token);

    String getUsernameFromJwt(String token);
    String getTypeFromJwt(String token);
}
