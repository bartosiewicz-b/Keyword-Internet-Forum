package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.JwtDto;
import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.EmailAlreadyTakenException;
import com.keyword.keywordspring.exception.InvalidUsernameOrPasswordException;
import com.keyword.keywordspring.exception.UsernameAlreadyTakenException;

public interface UserService {
    boolean register(RegisterRequest request) throws UsernameAlreadyTakenException, EmailAlreadyTakenException;

    JwtDto login(LoginRequest request) throws InvalidUsernameOrPasswordException;
}
