package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.JwtUtil;
import com.keyword.keywordspring.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostApi {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token,
            @RequestBody CreatePostRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            postService.createPost(user, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
