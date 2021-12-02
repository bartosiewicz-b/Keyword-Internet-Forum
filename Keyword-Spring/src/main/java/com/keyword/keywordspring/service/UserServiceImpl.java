package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ReturnValue;
import com.keyword.keywordspring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public ReturnValue<AppUser> login(LoginRequest request) {
        ReturnValue<AppUser> user = new ReturnValue<>();

        if(userRepository.findByUsername(request.getLogin()).isPresent())
            user.set(userRepository.findByUsername(request.getLogin()).get());
        else if (userRepository.findByEmail(request.getLogin()).isPresent())
            user.set(userRepository.findByEmail(request.getLogin()).get());

        if(user.isNok() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
            user.setError("Invalid credentials.");

        return user;
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
