package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.repository.CommentRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    CommentMapper commentMapper;
    @InjectMocks
    CommentServiceImpl commentService;

    Comment comment;

    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .id(1L)
                .user(AppUser.builder()
                        .id(1L)
                        .build())
                .content("Old content.")
                .build();
    }

    @Test
    void getComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder().build()));
        when(commentRepository.findAllByPost(any())).thenReturn(comments);

        List<CommentDto> result = commentService.getComments(1L);

        assertEquals(commentMapper.mapToDto(comment), result.get(0));
    }

    @Test
    void editComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        commentService.editComment(AppUser.builder().id(1L).build(),
                EditCommentRequest.builder().id(1L).newContent("New content.").build());
    }
}