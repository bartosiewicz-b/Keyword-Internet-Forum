package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangePasswordRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.model.AppUser;

import java.util.Optional;

public interface UserService {

    void register(RegisterRequest request);

    Optional<AppUser> login(LoginRequest request);

    TokenResponse changeUsername(String username, AppUser user);
    TokenResponse changeEmail(ChangeEmailRequest request, AppUser user);
    TokenResponse changePassword(ChangePasswordRequest request, AppUser user);

    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);

    UserDto getUser(String username);
}
