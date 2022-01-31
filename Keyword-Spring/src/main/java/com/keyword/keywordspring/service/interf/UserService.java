package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.model.AppUser;

import java.util.List;

public interface UserService {

    void register(RegisterRequest request);

    AppUser login(LoginRequest request);

    void changeUsername(String token, String newUsername);
    void changeEmail(String token, String password, String newEmail);
    void changePassword(String token, String password, String newPassword);

    boolean validateNewUsername(String username);
    boolean validateNewEmail(String email);

    UserDto get(String username);

    List<GroupDto> getSubscribedGroups(String token);
}
