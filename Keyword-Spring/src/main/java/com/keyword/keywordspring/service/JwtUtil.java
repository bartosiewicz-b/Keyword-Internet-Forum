package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.response.LoginResponse;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ReturnValue;

public interface JwtUtil {

    LoginResponse generateLoginResponse(AppUser user);

    ReturnValue<LoginResponse> refreshJwt(String refreshToken);

    boolean validateJwt(String token);

    AppUser getUserFromToken(String token);

    String getUsernameFromJwt(String token);
    String getTypeFromJwt(String token);
}
