package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class UserApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        try {
            return ResponseEntity.ok().body(userService.getUser(username));
        } catch( Exception e) {
            throw new RuntimeException();
        }
    }

    @GetMapping("/get-subscribed")
    public ResponseEntity<List<GroupDto>> getSubscribedGroups(@RequestHeader("Authorization") String token) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        return ResponseEntity.ok().body(userService.getSubscribedGroups(user));
    }
}
