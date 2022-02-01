package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.mapper.interf.UserMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
    JwtUtil jwtUtil;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper userMapper;
    @Mock
    GroupMapper groupMapper;
    @InjectMocks
    UserServiceImpl userService;

    RegisterRequest registerRequest;
    LoginRequest loginRequest;

    AppUser user;
    UserDto userDto;

    @BeforeEach
    void setUp() {

        registerRequest = RegisterRequest.builder().username("test").email("test@email.com")
                .password("password").build();

        loginRequest = LoginRequest.builder().login("test@email.com")
                .password("password").build();

        user = AppUser.builder()
                .id(1L)
                .username("username")
                .subscribedGroups(new ArrayList<>(){{
                    add(ForumGroup.builder().id("group").build());
                }})
                .build();

        userDto = UserDto.builder()
                .username(user.getUsername())
                .build();
    }

    @Test
    void registerOk() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(registerRequest.getPassword());

        userService.register(registerRequest);
    }

    @Test
    void registerUsernameTaken() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyTakenException.class,
                () -> userService.register(registerRequest));
    }

    @Test
    void registerEmailTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyTakenException.class,
                () -> userService.register(registerRequest));
    }

    @Test
    void loginOK() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userService.login(loginRequest);
    }

    @Test
    void loginNoUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class,
                () -> userService.login(loginRequest));
    }

    @Test
    void loginBadPassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> userService.login(loginRequest));
    }

    @Test
    void changeUsername() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        userService.changeUsername("token", "newUsername");
    }

    @Test
    void changeUsernameTaken() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyTakenException.class,
                () -> userService.changeUsername("token", "newUsername"));
    }

    @Test
    void changeEmail() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userService.changeEmail("token", "password", "newPassword");
    }

    @Test
    void changeEmailTaken() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyTakenException.class,
                () -> userService.changeEmail("token", "password", "newPassword"));
    }

    @Test
    void changeEmailBadPassword() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> userService.changeEmail("token", "password", "newEmail@email.com"));
    }

    @Test
    void changePassword() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userService.changePassword("token", "password", "newPassword");
    }

    @Test
    void changePasswordBad() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () ->userService.changePassword("token", "password", "newPassword"));
    }

    @Test
    void validateUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertTrue(userService.validateNewUsername("username"));
    }

    @Test
    void validateUsernameBad() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(AppUser.builder().build()));

        assertFalse(userService.validateNewUsername("username"));
    }

    @Test
    void validateEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertTrue(userService.validateNewEmail("email@email.com"));
    }

    @Test
    void validateEmailBad() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(AppUser.builder().build()));

        assertFalse(userService.validateNewEmail("email@email.com"));
    }

    @Test
    void get() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userMapper.mapToDto(any())).thenReturn(userDto);

        userService.get("username");
    }

    @Test
    void getSubscribedGroups() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(groupMapper.mapToDto(any(), any())).thenReturn(GroupDto.builder().id("group").build());

        List<GroupDto> res = userService.getSubscribedGroups("token");

        assertEquals(user.getSubscribedGroups().size(), res.size());
    }
}