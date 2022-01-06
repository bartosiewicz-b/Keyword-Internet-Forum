package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.CommentRepository;
import com.keyword.keywordspring.repository.CommentVoteRepository;
import com.keyword.keywordspring.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    CommentMapper commentMapper;
    @Mock
    CommentVoteRepository commentVoteRepository;
    @InjectMocks
    CommentServiceImpl commentService;

    Comment comment1;

    @BeforeEach
    void setUp() {
        comment1 = Comment.builder()
                .id(1L)
                .user(AppUser.builder()
                        .id(1L)
                        .build())
                .content("Old content.")
                .votes(0)
                .build();
    }

    @Test
    void getComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder().build()));
        when(commentRepository.findAllByPostOrderByVotesDesc(any())).thenReturn(comments);

        List<CommentDto> result = commentService.getComments(1L, comment1.getUser());

        assertEquals(commentMapper.mapToDto(comment1, comment1.getUser()), result.get(0));
    }

    @Test
    void editComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));

        commentService.editComment(AppUser.builder().id(1L).build(),
                EditCommentRequest.builder().id(1L).newContent("New content.").build());
    }

    @Test
    void upvoteComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.empty());

        commentService.upvote(comment1.getUser(), 1L);

        verify(commentVoteRepository, times(1)).save(any());
    }

    @Test
    void upvoteAlreadyUpvotedComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.UP)
                        .build()));

        commentService.upvote(comment1.getUser(), 1L);

        verify(commentVoteRepository, times(1)).delete(any());
    }

    @Test
    void upvoteAlreadyDownvotedComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        commentService.upvote(comment1.getUser(), 1L);

        verify(commentVoteRepository, times(1)).save(any());
    }

    @Test
    void downvoteComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.empty());

        commentService.downvote(comment1.getUser(), 1L);

        verify(commentVoteRepository, times(1)).save(any());
    }

    @Test
    void downvoteAlreadyDownvotedComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        commentService.downvote(comment1.getUser(), 1L);

        verify(commentVoteRepository, times(1)).delete(any());
    }

    @Test
    void downvoteAlreadyUpvotedComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));
        when(commentVoteRepository.findByUserAndComment(any(), any()))
                .thenReturn(Optional.of(CommentVote.builder()
                        .type(VoteType.UP)
                        .build()));

        commentService.downvote(comment1.getUser(), 1L);

        verify(commentVoteRepository, times(1)).save(any());
    }
}