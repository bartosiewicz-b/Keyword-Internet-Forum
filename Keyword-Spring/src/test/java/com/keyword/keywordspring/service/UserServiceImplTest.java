package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.RegisterRequest;
import com.keyword.keywordspring.exception.EmailAlreadyTakenException;
import com.keyword.keywordspring.exception.UsernameAlreadyTakenException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    UserService userService;

    RegisterRequest request;
    Optional<AppUser> userOptional;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);

        request = RegisterRequest.builder().username("test").email("test@email.com")
                .password("password").build();

        userOptional = Optional.of(new AppUser());
    }

    @Test
    void registerOk() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        boolean result = userService.register(request);

        assertEquals(result, true);
    }

    @Test
    void registerUsernameTaken() {

        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);

        assertThrows(UsernameAlreadyTakenException.class,
                () -> userService.register(request));
    }

    @Test
    void registerEmailTaken() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(userOptional);

        assertThrows(EmailAlreadyTakenException.class,
                () -> userService.register(request));
    }
}