package com.keyword.keywordspring.service;

import com.keyword.keywordspring.model.AppUser;

public interface JwtUtil {

    String generateJwt(AppUser user);

    boolean validateJwt(String token) throws Exception;

    String getUsernameFromJwt(String token);
}
