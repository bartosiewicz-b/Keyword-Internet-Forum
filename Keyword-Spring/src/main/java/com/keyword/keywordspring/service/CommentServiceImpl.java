package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.repository.CommentRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.service.interf.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

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
                        .dateCreated(new Date(System.currentTimeMillis()))
                .build());

    }

    @Override
    public List<CommentDto> getComments(Integer page) {

        return commentRepository.findAll(PageRequest.of(page, 10))
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
