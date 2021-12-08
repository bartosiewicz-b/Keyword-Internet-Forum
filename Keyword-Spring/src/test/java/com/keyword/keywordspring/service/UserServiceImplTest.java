package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    RegisterRequest registerRequest;
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        registerRequest = RegisterRequest.builder().username("test").email("test@email.com")
                .password("password").build();

        loginRequest = LoginRequest.builder().login("test@email.com")
                .password("password").build();
    }

    @Test
    void registerOk() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        userService.register(registerRequest);
    }

    @Test
    void registerUsernameTaken() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new AppUser()));

        assertThrows(UsernameAlreadyTakenException.class,
                () -> userService.register(registerRequest));
    }

    @Test
    void registerEmailTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new AppUser()));

        assertThrows(EmailAlreadyTakenException.class,
                () -> userService.register(registerRequest));
    }

    @Test
    void loginOK() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(AppUser.builder()
                .username("username").email("email@email.com").password("password").build()));

        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userService.login(loginRequest);
    }

    @Test
    void loginBadPassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(AppUser.builder()
                .username("username").email("email@email.com").password("password").build()));

        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertFalse(userService.login(loginRequest).isPresent());
    }

    @Test
    void loginNoUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertFalse(userService.login(loginRequest).isPresent());
    }

    @Test
    void isUsernameTakenOk() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertFalse(userService.isUsernameTaken("username"));
    }

    @Test
    void isUsernameTakenBad() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(AppUser.builder().build()));

        assertTrue(userService.isUsernameTaken("username"));
    }

    @Test
    void isEmailTakenOk() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertFalse(userService.isEmailTaken("email@email.com"));
    }

    @Test
    void isEmailTakenBad() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(AppUser.builder().build()));

        assertTrue(userService.isEmailTaken("email@email.com"));
    }
}