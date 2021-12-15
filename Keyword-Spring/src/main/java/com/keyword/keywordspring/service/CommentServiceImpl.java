package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
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
import java.util.Objects;
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
        Optional<Comment> comment = request.getParentCommentId() != null ?
                commentRepository.findById(request.getParentCommentId()) :
                Optional.empty();

        if(post.isEmpty())
            throw new PostDoesNotExistException(request.getPostId());

        commentRepository.save(Comment.builder()
                        .content(request.getContent())
                        .user(user)
                        .post(post.get())
                        .parentComment(comment.orElse(null))
                        .dateCreated(new Date(System.currentTimeMillis()))
                        .edited(false)
                .build());

    }

    @Override
    public List<CommentDto> getComments(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostDoesNotExistException(postId));

        return commentRepository.findAllByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void editComment(AppUser user, EditCommentRequest request) {

        if(commentRepository.findById(request.getId()).isEmpty() ||
                !Objects.equals(user.getId(), commentRepository.findById(request.getId()).get().getUser().getId()))
            throw new UnauthorizedException();

        Comment comment = commentRepository.findById(request.getId())
                .orElseThrow(() -> new CommentDoesNotExistException(request.getId()));

        comment.setContent(request.getNewContent());
        comment.setEdited(true);

        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(AppUser user, Long id) {

        if(commentRepository.findById(id).isEmpty() ||
                !Objects.equals(user.getId(), commentRepository.findById(id).get().getUser().getId()))
            throw new UnauthorizedException();

        commentRepository.deleteById(id);
    }
}
