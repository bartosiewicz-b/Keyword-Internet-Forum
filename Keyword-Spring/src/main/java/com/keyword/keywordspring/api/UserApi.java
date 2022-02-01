package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserApi {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {

        return ResponseEntity.ok().body(userService.get(username));
    }

    @GetMapping("/get-subscribed")
    public ResponseEntity<List<GroupDto>> getSubscribedGroups(@RequestHeader("Authorization") String token) {

        return ResponseEntity.ok().body(userService.getSubscribedGroups(token));
    }
}
