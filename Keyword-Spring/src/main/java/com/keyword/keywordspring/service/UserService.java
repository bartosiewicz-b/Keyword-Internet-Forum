package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;

public interface UserService {

    void register(RegisterRequest request);

    AppUser login(LoginRequest request);

    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
