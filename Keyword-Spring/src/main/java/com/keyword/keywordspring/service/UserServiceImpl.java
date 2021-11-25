package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        validateNewEmail(request.getEmail());
        validateNewUsername(request.getUsername());
        validateNewPassword(request.getPassword());

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
    //Must be 4-32 characters long and contain only letters or numbers
    public void validateNewUsername(String username) {

        if(userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyTakenException(username);

        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(username);

        if(username.length() < 4 || username.length() > 32 || matcher.find())
            throw new IllegalUsernameException(username);
    }

    @Override
    public void validateNewEmail(String email) {

        if(userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyTakenException(email);

        if(!email.contains("@"))
            throw new IllegalEmailException(email);
    }

    @Override
    //Must be at least 8 characters long
    public void validateNewPassword(String password) {
        if(password.length() < 8)
            throw new IllegalPasswordException();
    }
}
