package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.dto.request.IdRequest;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class PostApi {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@RequestHeader("Authorization") String token,
            @RequestBody CreatePostRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);
        return ResponseEntity.ok().body(postService.createPost(user, request));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PostDto>> getPosts(@RequestHeader(value = "Authorization", required = false) String token,
                            @RequestParam Integer page,
                            @RequestParam(required = false) String groupId,
                            @RequestParam(required = false) String name) {

        AppUser user = jwtUtil.getUserFromToken(token);
        return ResponseEntity.ok().body(postService.getPosts(page, name, groupId, user));
    }

    @GetMapping("/get-all-count")
    public ResponseEntity<Integer> getPostsCount(@RequestParam(required = false) String groupId,
                                                  @RequestParam(required = false) String name) {

        return ResponseEntity.ok().body(postService.getPostsCount(groupId, name));
    }

    @GetMapping("/get")
    public ResponseEntity<PostDto> getPost(@RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam Long id) {

        AppUser user = jwtUtil.getUserFromToken(token);
        return ResponseEntity.ok().body(postService.getPost(id, user));
    }

    @PostMapping("/edit")
    public ResponseEntity<Long> editPost(@RequestHeader("Authorization") String token,
                                           @RequestBody EditPostRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);
        return ResponseEntity.ok().body(postService.editPost(user, request));
    }

    @PostMapping("/upvote")
    public ResponseEntity<String> upvote(@RequestHeader("Authorization") String token,
                                         @RequestBody Map<String, Long> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        postService.upvote(user, request.get("postId"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/downvote")
    public ResponseEntity<Void> downvote(@RequestHeader("Authorization") String token,
                                         @RequestBody Map<String, Long> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        postService.downvote(user, request.get("postId"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token,
                                             @RequestBody IdRequest request) {
        AppUser user = jwtUtil.getUserFromToken(token);

        postService.deletePost(user, request.getId());
        return ResponseEntity.ok().build();
    }
}
