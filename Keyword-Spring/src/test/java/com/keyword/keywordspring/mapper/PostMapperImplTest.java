package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostMapperImplTest {

    @InjectMocks
    PostMapperImpl postMapper;

    Post post;

    @BeforeEach
    void setUp() {
        ForumGroup group = ForumGroup.builder()
                .id("group")
                .groupName("group")
                .build();

        AppUser user = AppUser.builder()
                .username("username")
                .build();

        post = Post.builder()
                .id(1L)
                .forumGroup(group)
                .title("title")
                .description("description")
                .dateCreated(null)
                .user(user)
                .comments(new ArrayList<>())
                .votes(0)
                .build();
    }

    @Test
    void mapToDto() {
        PostDto res = postMapper.mapToDto(post, null);

        assertNotNull(res);
        assertEquals(post.getId(), res.getId());
        assertEquals(post.getForumGroup().getGroupName(), res.getGroupName());
        assertEquals(post.getForumGroup().getId(), res.getGroupId());
        assertEquals(post.getTitle(), res.getTitle());
        assertEquals(post.getDescription(), res.getDescription());
        assertEquals(post.getDateCreated(), res.getDateCreated());
        assertEquals(post.getUser().getUsername(), res.getUsername());
        assertEquals(post.getComments().size(), res.getNumberOfComments());
        assertEquals(post.getVotes(), res.getVotes());
        assertNull(res.getUserVote());
    }
}