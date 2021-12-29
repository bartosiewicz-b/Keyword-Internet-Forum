package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.dto.request.IdRequest;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.VoteType;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(postService.createPost(user, request));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PostDto>> getPosts(@RequestHeader(value = "Authorization", required = false) String token,
                            @RequestParam Integer page,
                            @RequestParam(required = false) String name) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(postService.getPosts(page, name, user));
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }

    }

    @GetMapping("/get")
    public ResponseEntity<PostDto> getPost(@RequestHeader(value = "Authorization", required = false) String token,
                            @RequestParam Long id) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(postService.getPost(id, user));
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }

    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editPost(@RequestHeader("Authorization") String token,
                                           @RequestBody EditPostRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            postService.editPost(user, request);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/upvote")
    public ResponseEntity<Void> upvote(@RequestHeader("Authorization") String token,
                                         @RequestBody IdRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            postService.vote(user, request.getId(), VoteType.UP);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/downvote")
    public ResponseEntity<Void> downvote(@RequestHeader("Authorization") String token,
                                         @RequestBody IdRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            postService.vote(user, request.getId(), VoteType.DOWN);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token,
                                             @RequestBody IdRequest request) {
        try{
            AppUser user = jwtUtil.getUserFromToken(token);

            postService.deletePost(user, request.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }
}
