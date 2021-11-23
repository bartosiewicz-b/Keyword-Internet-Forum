package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.LoginRequest;
import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    UserService userService;

    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    Optional<AppUser> userOptional;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);

        registerRequest = RegisterRequest.builder().username("test").email("test@email.com")
                .password("password").build();

        loginRequest = LoginRequest.builder().login("test@email.com")
                .password("password").build();

        userOptional = Optional.of(new AppUser());
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

        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);

        assertThrows(UsernameAlreadyTakenException.class,
                () -> userService.register(registerRequest));
    }

    @Test
    void registerEmailTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(userOptional);

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

        assertThrows(InvalidUsernameOrPasswordException.class, () -> userService.login(loginRequest));
    }

    @Test
    void loginNoUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidUsernameOrPasswordException.class, () -> userService.login(loginRequest));
    }

    @Test
    void validateNewUsernameOK() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        userService.validateNewUsername("username");
    }

    @Test
    void validateNewUsernameBad() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());


        assertThrows(IllegalUsernameException.class, () -> userService.validateNewUsername("u"));
        assertThrows(IllegalUsernameException.class, () -> userService.validateNewUsername("........"));
    }

    @Test
    void validateNewUsernameTaken() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(
                AppUser.builder().username("username").build()));

        assertThrows(UsernameAlreadyTakenException.class, () -> userService.validateNewUsername("username"));
    }

    @Test
    void validateNewEmailOK() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.validateNewEmail("email@email.com");
    }

    @Test
    void validateNewEmailBad() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalEmailException.class, () -> userService.validateNewEmail("email"));
    }

    @Test
    void validateNewEmailTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(
                AppUser.builder().email("email@email.com").build()));

        assertThrows(EmailAlreadyTakenException.class, () ->
                userService.validateNewEmail("email@email.com"));
    }

    @Test
    void validateNewPasswordOK() {
        userService.validateNewPassword("password");
    }

    @Test
    void validateNewPasswordBad() {
        assertThrows(IllegalPasswordException.class, () -> userService.validateNewPassword("p"));
    }
}