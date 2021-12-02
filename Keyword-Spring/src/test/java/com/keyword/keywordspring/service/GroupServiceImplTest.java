package com.keyword.keywordspring.service;

import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository groupRepository;

    GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        groupService = new GroupServiceImpl(groupRepository);
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