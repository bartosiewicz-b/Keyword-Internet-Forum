package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
import com.keyword.keywordspring.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class UserApi {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        try {
            return ResponseEntity.ok().body(userService.getUser(username));
        } catch( Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }
}
