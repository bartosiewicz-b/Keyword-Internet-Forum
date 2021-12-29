package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangePasswordRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.dto.response.AuthResponse;
import com.keyword.keywordspring.model.AppUser;

import java.util.Optional;

public interface UserService {

    void register(RegisterRequest request);

    Optional<AppUser> login(LoginRequest request);

    AuthResponse changeUsername(String username, AppUser user);
    AuthResponse changeEmail(ChangeEmailRequest request, AppUser user);
    AuthResponse changePassword(ChangePasswordRequest request, AppUser user);

    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
