package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.repository.CommentVoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperImplTest {

    @Mock
    CommentVoteRepository commentVoteRepository;
    @InjectMocks
    CommentMapperImpl commentMapper;

    Comment comment;

    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .id(1L)
                .content("content")
                .user(AppUser.builder().username("username").build())
                .post(Post.builder().id(1L).build())
                .votes(0)
                .build();
    }

    @Test
    void mapToDto() {

        when(commentVoteRepository.findByUserAndComment(any(), any())).thenReturn(Optional.empty());

        CommentDto res = commentMapper.mapToDto(comment, null);

        assertNotNull(res);
        assertEquals(comment.getId(), res.getId());
        assertEquals(comment.getContent(), res.getContent());
        assertEquals(comment.getUser().getUsername(), res.getUser());
        assertEquals(comment.getPost().getId(), res.getPostId());
        assertEquals(comment.getVotes(), res.getVotes());
    }
}