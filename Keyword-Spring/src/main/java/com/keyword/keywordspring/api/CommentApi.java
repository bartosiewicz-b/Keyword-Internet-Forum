package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.CommentService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
@Slf4j
public class CommentApi {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestHeader("Authorization") String token,
                                           @RequestBody CreateCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(commentService.addComment(user, request));
    }

    @GetMapping("/get")
    public ResponseEntity<List<CommentDto>> getComments(@RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam Long postId) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(commentService.getComments(postId, user));
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editComment(@RequestHeader("Authorization") String token,
                                         @RequestBody EditCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        commentService.editComment(user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upvote")
    public ResponseEntity<String> upvote(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, Long> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        commentService.upvote(user, request.get("commentId"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/downvote")
    public ResponseEntity<Void> downvote(@RequestHeader("Authorization") String token,
                                       @RequestBody Map<String, Long> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        commentService.downvote(user, request.get("commentId"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String token,
                                              @RequestBody Map<String, Long> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        commentService.deleteComment(user, request.get("id"));
        return ResponseEntity.ok().build();
    }
}
