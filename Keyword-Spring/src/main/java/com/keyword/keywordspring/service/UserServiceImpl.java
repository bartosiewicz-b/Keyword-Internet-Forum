package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.JwtDto;
import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.EmailAlreadyTakenException;
import com.keyword.keywordspring.exception.InvalidUsernameOrPasswordException;
import com.keyword.keywordspring.exception.UsernameAlreadyTakenException;
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
    public boolean register(RegisterRequest request) throws UsernameAlreadyTakenException, EmailAlreadyTakenException {

        if(userRepository.findByUsername(request.getUsername()).isPresent())
            throw new UsernameAlreadyTakenException(request.getUsername());

        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new EmailAlreadyTakenException(request.getEmail());

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return true;
    }

    @Override
    public JwtDto login(LoginRequest request) throws InvalidUsernameOrPasswordException {

        AppUser user;

        if(userRepository.findByUsername(request.getLogin()).isPresent())
            user = userRepository.findByUsername(request.getLogin()).get();
        else if (userRepository.findByEmail(request.getLogin()).isPresent())
            user = userRepository.findByEmail(request.getLogin()).get();
        else
            throw new InvalidUsernameOrPasswordException();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidUsernameOrPasswordException();

        return new JwtDto();
    }
}
