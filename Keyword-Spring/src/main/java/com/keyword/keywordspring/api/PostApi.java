package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostApi {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String token,
            @RequestBody CreatePostRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            postService.createPost(user, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam Integer page,
                                                  @RequestParam(required = false) String name) {

        try {
            return ResponseEntity.ok().body(postService.getPosts(page, name));
        } catch(Exception ignored) {
            return ResponseEntity.badRequest().build();
        }

    }
}
