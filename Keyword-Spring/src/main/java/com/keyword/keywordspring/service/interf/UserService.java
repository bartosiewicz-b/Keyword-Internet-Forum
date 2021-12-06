package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangeUsernameRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ReturnValue;

public interface UserService {

    void register(RegisterRequest request);

    ReturnValue<AppUser> login(LoginRequest request);

    void changeUsername(ChangeUsernameRequest request);
    void changeEmail(ChangeEmailRequest request);

    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
