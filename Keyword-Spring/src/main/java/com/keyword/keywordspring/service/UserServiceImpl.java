package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.exception.*;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.mapper.interf.UserMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;

    @Override
    public void register(RegisterRequest request) {
        if(isEmailTaken(request.getEmail()))
            throw new EmailAlreadyTakenException(request.getEmail());

        if(isUsernameTaken(request.getUsername()))
            throw new UsernameAlreadyTakenException(request.getUsername());

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .dateCreated(new Date(System.currentTimeMillis()))
                .nrOfComments(0)
                .nrOfPosts(0)
                .subscribed(new ArrayList<>())
                .moderatedGroups(new ArrayList<>())
                .password(passwordEncoder.encode(request.getPassword())).build();

        userRepository.save(user);
    }

    @Override
    public AppUser login(LoginRequest request) {

        Optional<AppUser> user = userRepository.findByUsername(request.getLogin());

        if(user.isEmpty())
            user = userRepository.findByEmail(request.getLogin());

        if(user.isEmpty()) throw new UnauthorizedException(request.getLogin());

        if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
            throw new UnauthorizedException(user.get().getUsername());

        return user.get();
    }

    @Override
    public void changeUsername(String token, String newUsername) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        if(isUsernameTaken(newUsername))
            throw new UsernameAlreadyTakenException(newUsername);

        user.setUsername(newUsername);

        userRepository.save(user);
    }

    @Override
    public void changeEmail(String token, String password, String newEmail) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        if(isEmailTaken(newEmail))
            throw new EmailAlreadyTakenException(newEmail);

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new UnauthorizedException(user.getUsername());

        user.setEmail(newEmail);

        userRepository.save(user);
    }

    @Override
    public void changePassword(String token, String password, String newPassword) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new UnauthorizedException(user.getUsername());

        user.setPassword(passwordEncoder.encode(newPassword));

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

    @Override
    public UserDto get(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistException(username));

        return userMapper.mapToDto(user);
    }

    @Override
    public List<GroupDto> getSubscribedGroups(String token) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        return user.getSubscribed().stream()
                .map(g -> groupMapper.mapToDto(g, user))
                .collect(Collectors.toList());
    }
}
