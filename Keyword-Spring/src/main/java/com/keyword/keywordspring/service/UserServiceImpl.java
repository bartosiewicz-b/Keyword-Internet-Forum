package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangeUsernameRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        if(isEmailTaken(request.getEmail()))
            throw new EmailAlreadyTakenException(request.getEmail());

        if(isUsernameTaken(request.getUsername()))
            throw new UsernameAlreadyTakenException(request.getUsername());

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).build();

        userRepository.save(user);
    }

    @Override
    public Optional<AppUser> login(LoginRequest request) {
        Optional<AppUser> user = Optional.empty();

        if(userRepository.findByUsername(request.getLogin()).isPresent())
            user = userRepository.findByUsername(request.getLogin());
        else if (userRepository.findByEmail(request.getLogin()).isPresent())
            user = userRepository.findByEmail(request.getLogin());

        if(user.isPresent() && !passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
            user = Optional.empty();

        return user;
    }

    @Override
    public void changeUsername(ChangeUsernameRequest request) {

        if(isUsernameTaken(request.getNewUsername()))
            throw new UsernameAlreadyTakenException(request.getNewUsername());

        AppUser user = login(LoginRequest.builder()
                .login(request.getEmail())
                .password(request.getPassword())
                .build())
                .orElseThrow(UnauthorizedException::new);

        user.setUsername(request.getNewUsername());

        userRepository.save(user);
    }

    @Override
    public void changeEmail(ChangeEmailRequest request) {

        if(isEmailTaken(request.getNewEmail()))
            throw new EmailAlreadyTakenException(request.getNewEmail());

        AppUser user = login(LoginRequest.builder()
                .login(request.getEmail())
                .password(request.getPassword())
                .build())
                .orElseThrow(UnauthorizedException::new);

        user.setEmail(request.getNewEmail());

        userRepository.save(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {

        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean isEmailTaken(String email) {

        return userRepository.findByEmail(email).isPresent();
    }
}
