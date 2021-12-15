package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.CommentService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class CommentApi {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<Void> createComment(@RequestHeader("Authorization") String token,
                                           @RequestBody CreateCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            commentService.addComment(user, request);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam Long postId) {

        try {
            return ResponseEntity.ok().body(commentService.getComments(postId));
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editComment(@RequestHeader("Authorization") String token,
                                         @RequestBody EditCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try{
            commentService.editComment(user, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String token,
                                              @RequestBody Map<String, Long> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try{
            commentService.deleteComment(user, request.get("id"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }
}
