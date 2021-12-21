package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository groupRepository;
    @Mock
    GroupMapper groupMapper;
    @InjectMocks
    GroupServiceImpl groupService;

    List<ForumGroup> groups;

    @BeforeEach
    void setUp() {
        groups = new ArrayList<>();
        groups.add(ForumGroup.builder()
                        .id("1")
                        .groupName("first group")
                .build());
        groups.add(ForumGroup.builder()
                .id("2")
                .groupName("second group")
                .build());
    }

    @Test
    void getGroups() {
        when(groupRepository.findAll(any())).thenReturn(groups);

        List<GroupDto> result = groupService.getGroups(0, null, null);

        assertEquals(result, groups.stream().map(res -> groupMapper.mapToDto(res, null)).collect(Collectors.toList()));
    }

    @Test
    void getGroupsLike() {
        when(groupRepository.findByGroupNameLike(anyString(), any())).thenReturn(groups);

        List<GroupDto> result = groupService.getGroups(0, "name", null);

        assertEquals(result, groups.stream().map(res -> groupMapper.mapToDto(res, null)).collect(Collectors.toList()));
    }

    @Test
    void isGroupNameTakenOk() {
        when(groupRepository.findByGroupName(anyString())).thenReturn(Optional.empty());

        assertFalse(groupService.isGroupNameTaken("group"));
    }

    @Test
    void isGroupNameTakenNok() {
        when(groupRepository.findByGroupName(anyString())).thenReturn(Optional.of(ForumGroup.builder().build()));

        assertTrue(groupService.isGroupNameTaken("group"));
    }
}