package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.AddCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.CommentRepository;
import com.keyword.keywordspring.repository.CommentVoteRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    CommentVoteRepository commentVoteRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    CommentMapper commentMapper;

    @InjectMocks
    CommentServiceImpl commentService;

    AppUser user;
    AppUser badUser;
    Post post;
    Comment comment;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {

        user = AppUser.builder().id(1L).build();

        badUser = AppUser.builder().id(2L).username("badUser").build();

        post = Post.builder()
                .id(1L)
                .build();

        comment = Comment.builder()
                .id(1L)
                .content("comment")
                .user(AppUser.builder().id(1L).build())
                .post(post)
                .edited(false)
                .votes(0)
                .build();

        commentDto = CommentDto.builder()
                .id(comment.getId())
                .user(comment.getUser().getUsername())
                .build();
    }

    @Test
    void addComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentMapper.mapToDto(any(), any())).thenReturn(commentDto);

        AddCommentRequest request = AddCommentRequest.builder()
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .build();

        CommentDto res = commentService.add("token", request);

        assertNotNull(res);
        assertEquals(comment.getId(), res.getId());
    }

    @Test
    void getAllComments() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findAllByPostOrderByVotesDesc(any()))
                .thenReturn(new ArrayList<>(){{add(comment);}});
        when(commentMapper.mapToDto(any(),any())).thenReturn(commentDto);

        List<CommentDto> res = commentService.getAll("token", post.getId());

        assertNotNull(res);
        assertEquals(commentDto, res.get(0));
    }

    @Test
    void editComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        commentService.edit("token",
                EditCommentRequest.builder()
                        .id(comment.getId())
                        .newContent("new content")
                        .build());
    }

    @Test
    void editCommentWrongUser() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(badUser));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedException.class, () -> commentService.edit("token",
                EditCommentRequest.builder()
                        .id(comment.getId())
                        .newContent("new content")
                        .build()));
    }

    @Test
    void deleteCommentWrongUser() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(badUser));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedException.class, () ->
                commentService.delete("token", comment.getId()));
    }

    @Test
    void upvoteComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentVoteRepository.findByUserAndComment(any(), any())).thenReturn(Optional.empty());

        commentService.upvote("token", comment.getId());

        verify(commentVoteRepository, times(1)).save(any());
    }

    @Test
    void upvoteAlreadyUpvotedComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.UP)
                        .build()));

        commentService.upvote("token", comment.getId());

        verify(commentVoteRepository, times(1)).delete(any());
    }

    @Test
    void upvoteAlreadyDownvotedComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        commentService.upvote("token", comment.getId());

        verify(commentVoteRepository, times(1)).save(any());
    }

    @Test
    void downvoteComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.empty());

        commentService.downvote("token", comment.getId());

        verify(commentVoteRepository, times(1)).save(any());
    }

    @Test
    void downvoteAlreadyDownvotedComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        commentService.downvote("token", comment.getId());

        verify(commentVoteRepository, times(1)).delete(any());
    }

    @Test
    void downvoteAlreadyUpvotedComment() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.UP)
                        .build()));

        commentService.downvote("token", comment.getId());

        verify(commentVoteRepository, times(1)).save(any());
    }
}