package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.AddPostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.*;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.repository.PostVoteRepository;
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
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @Mock
    PostMapper postMapper;
    @Mock
    GroupRepository groupRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    PostVoteRepository postVoteRepository;
    @InjectMocks
    PostServiceImpl postService;

    AppUser user;
    AppUser badUser;

    ForumGroup group;

    Post post;
    PostDto postDto;

    @BeforeEach
    void setUp() {

        user = AppUser.builder()
                .id(1L)
                .username("user")
                .build();

        badUser = AppUser.builder()
                .id(2L)
                .username("badUser")
                .build();

        group = ForumGroup.builder()
                .id("group")
                .groupName("group")
                .moderators(new ArrayList<>() {{add(user);}})
                .build();

        post = Post.builder()
                .id(1L)
                .title("Old title")
                .description("Old description")
                .user(user)
                .votes(10)
                .forumGroup(group)
                .build();

        postDto = PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .groupName(post.getForumGroup().getGroupName())
                .build();
    }

    @Test
    void addPost() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(groupRepository.findById(anyString())).thenReturn(Optional.of(group));
        when(postMapper.mapToDto(any(), any())).thenReturn(postDto);

        AddPostRequest request = AddPostRequest.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .groupId(post.getForumGroup().getId())
                .build();

        PostDto res = postService.add("token", request);

        assertNotNull(res);
        assertEquals(postDto, res);
    }

    @Test
    void getAllPosts() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findAll(any())).thenReturn(new ArrayList<>(){{add(post);}});
        when(postMapper.mapToDto(any(),any())).thenReturn(postDto);

        List<PostDto> res = postService.getAll("token", null, 0, null);

        assertNotNull(res);
        assertEquals(postDto, res.get(0));
    }

    @Test
    void getCountPosts() {

        when(postRepository.findAll()).thenReturn(new ArrayList<>(){{add(post);}});

        int res = postService.getCount(null, null);

        assertEquals(1, res);
    }

    @Test
    void getPost() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postMapper.mapToDto(any(),any())).thenReturn(postDto);

        PostDto res = postService.get("token", post.getId());

        assertNotNull(res);
        assertEquals(postDto, res);
    }

    @Test
    void editPost() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        postService.edit("token", EditPostRequest.builder()
                        .postId(post.getId())
                        .title("new title")
                        .description("new description")
                        .build());
    }

    @Test
    void editPostWrongUser() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(badUser));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThrows(UnauthorizedException.class, () -> postService.edit("token",
                EditPostRequest.builder()
                        .postId(post.getId())
                        .title("new title")
                        .description("new description")
                        .build()));
    }

    @Test
    void deletePostWrongUser() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(badUser));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThrows(UnauthorizedException.class, () ->
                postService.delete("token", post.getId()));
    }

    @Test
    void upvotePost() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());

        postService.upvote("token", post.getId());

        verify(postVoteRepository, times(1)).save(any());
    }

    @Test
    void upvoteAlreadyUpvotedPost() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.UP)
                        .build()));

        postService.upvote("token", post.getId());

        verify(postVoteRepository, times(1)).delete(any());
    }

    @Test
    void upvoteAlreadyDownvotedPost() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        postService.upvote("token", post.getId());

        verify(postVoteRepository, times(1)).save(any());
    }

    @Test
    void downvotePost() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.empty());

        postService.downvote("token", post.getId());

        verify(postVoteRepository, times(1)).save(any());
    }

    @Test
    void downvoteAlreadyDownvotedPost() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.DOWN)
                        .build()));

        postService.downvote("token", post.getId());

        verify(postVoteRepository, times(1)).delete(any());
    }

    @Test
    void downvoteAlreadyUpvotedPost() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postVoteRepository.findByUserAndPost(any(), any()))
                .thenReturn(Optional.of(PostVote.builder()
                        .type(VoteType.UP)
                        .build()));

        postService.downvote("token", post.getId());

        verify(postVoteRepository, times(1)).save(any());
    }
}