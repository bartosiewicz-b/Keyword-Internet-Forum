package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupMapperImplTest {

    @InjectMocks
    GroupMapperImpl groupMapper;

    ForumGroup group;

    @BeforeEach
    void setUp() {
        group = ForumGroup.builder()
                .id("group")
                .groupName("group")
                .description("description")
                .subscriptions(0)
                .owner(AppUser.builder().username("username").build())
                .moderators(new ArrayList<>())
                .build();
    }

    @Test
    void mapToDto() {
        GroupDto res = groupMapper.mapToDto(group, null);

        assertNotNull(res);
        assertEquals(group.getId(), res.getId());
        assertEquals(group.getGroupName(), res.getGroupName());
        assertEquals(group.getDescription(), res.getDescription());
        assertEquals(group.getSubscriptions(), res.getSubscriptions());
        assertEquals(group.getOwner().getUsername(), res.getOwner());
        assertEquals(group.getModerators().size(), res.getModerators().size());
    }
}