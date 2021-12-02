package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.repository.CommentRepository;
import com.keyword.keywordspring.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public void addComment(AppUser user, CreateCommentRequest request) {

        Optional<Post> post = postRepository.findById(request.getPostId());
        Optional<Comment> comment = commentRepository.findById(request.getParentCommentId());

        if(post.isEmpty())
            throw new PostDoesNotExistException(request.getPostId());

        commentRepository.save(Comment.builder()
                        .content(request.getContent())
                        .user(user)
                        .post(post.get())
                        .parentComment(comment.orElse(null))
                .build());

    }
}
