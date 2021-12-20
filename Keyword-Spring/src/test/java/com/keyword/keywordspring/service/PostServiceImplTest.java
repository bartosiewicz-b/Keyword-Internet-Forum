package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.repository.PostVoteRepository;
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
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @Mock
    PostMapper postMapper;
    @Mock
    PostVoteRepository postVoteRepository;
    @InjectMocks
    PostServiceImpl postService;

    Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("Old title")
                .description("Old description")
                .user(AppUser.builder()
                        .id(1L)
                        .build())
                .votes(10)
                .build();
    }

    @Test
    void getPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.findAll(any())).thenReturn(posts);

        List<PostDto> result = postService.getPosts(0, null, null);

        assertEquals(postMapper.mapToDto(post, post.getUser()), result.get(0));
    }

    @Test
    void editPost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        postService.editPost(AppUser.builder().id(1L).build(),
                EditPostRequest.builder().id(1L).title("New title").description("new description").build());
    }

    @Test
    void upvotePost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.empty());

        postService.upvote(post.getUser(), 1L);

        verify(postVoteRepository, times(1)).save(any());
    }

    @Test
    void upvoteAlreadyUpvotedPost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.UP)
                        .build()));

        postService.upvote(post.getUser(), 1L);

        verify(postVoteRepository, times(1)).delete(any());
    }

    @Test
    void upvoteAlreadyDownvotedPost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        postService.upvote(post.getUser(), 1L);

        verify(postVoteRepository, times(1)).save(any());
    }

    @Test
    void downvotePost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.empty());

        postService.downvote(post.getUser(), 1L);

        verify(postVoteRepository, times(1)).save(any());
    }

    @Test
    void downvoteAlreadyDownvotedPost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        postService.downvote(post.getUser(), 1L);

        verify(postVoteRepository, times(1)).delete(any());
    }

    @Test
    void downvoteAlreadyUpvotedPost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.UP)
                        .build()));

        postService.downvote(post.getUser(), 1L);

        verify(postVoteRepository, times(1)).save(any());
    }
}