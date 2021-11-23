package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;

public interface UserService {
    void register(RegisterRequest request);

    void login(LoginRequest request);

    void validateNewUsername(String username);
    void validateNewEmail(String email);
    void validateNewPassword(String password);
}
