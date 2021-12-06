package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Post;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @Mock
    PostMapper postMapper;
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
                .build();
    }

    @Test
    void getPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.findAll(any())).thenReturn(posts);

        List<PostDto> result = postService.getPosts(0, null);

        assertEquals(postMapper.mapToDto(post), result.get(0));
    }

    @Test
    void editPost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        postService.editPost(AppUser.builder().id(1L).build(),
                EditPostRequest.builder().id(1L).title("New title").description("new description").build());
    }
}