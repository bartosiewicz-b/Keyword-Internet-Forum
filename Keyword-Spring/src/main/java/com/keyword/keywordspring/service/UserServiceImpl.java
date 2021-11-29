package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.model.AppUser;
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
    public AppUser login(LoginRequest request) {
        AppUser user;

        if(userRepository.findByUsername(request.getLogin()).isPresent())
            user = userRepository.findByUsername(request.getLogin()).get();
        else if (userRepository.findByEmail(request.getLogin()).isPresent())
            user = userRepository.findByEmail(request.getLogin()).get();
        else
            throw new InvalidUsernameOrPasswordException();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidUsernameOrPasswordException();

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
