package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginResponse;
import com.keyword.keywordspring.model.AppUser;

import java.util.Date;

public interface JwtUtil {

    String generateJwt(AppUser user);

    boolean validateJwt(String token);

    String getUsernameFromJwt(String token);
    String getTypeFromJwt(String token);
    Date getIssuedAtFromJwt(String token );

    String generateRefresh(AppUser user);

    LoginResponse refreshJwt(String refreshToken);

    boolean validateRefresh(String refreshToken);
}
