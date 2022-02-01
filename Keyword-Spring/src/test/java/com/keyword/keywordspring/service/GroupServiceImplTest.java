package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.AddGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.mapper.interf.UserMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository groupRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    GroupMapper groupMapper;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    GroupServiceImpl groupService;

    AppUser owner;
    AppUser moderator;
    AppUser subscriber;
    AppUser nonSubscriber;

    ForumGroup group;
    GroupDto groupDto;

    @BeforeEach
    void setUp() {

        owner = AppUser.builder()
                .id(1L)
                .username("username1")
                .ownedGroups(new ArrayList<>(){{add(ForumGroup.builder().id("group").build());}})
                .moderatedGroups(new ArrayList<>(){{add(ForumGroup.builder().id("group").build());}})
                .subscribedGroups(new ArrayList<>(){{add(ForumGroup.builder().id("group").build());}})
                .build();

        moderator = AppUser.builder()
                .id(2L)
                .username("username2")
                .ownedGroups(new ArrayList<>())
                .moderatedGroups(new ArrayList<>(){{add(ForumGroup.builder().id("group").build());}})
                .subscribedGroups(new ArrayList<>(){{add(ForumGroup.builder().id("group").build());}})
                .build();

        subscriber = AppUser.builder()
                .id(3L)
                .username("username3")
                .ownedGroups(new ArrayList<>())
                .moderatedGroups(new ArrayList<>())
                .subscribedGroups(new ArrayList<>(){{add(ForumGroup.builder().id("group").build());}})
                .build();

        nonSubscriber = AppUser.builder()
                .id(4L)
                .username("username4")
                .ownedGroups(new ArrayList<>())
                .moderatedGroups(new ArrayList<>())
                .subscribedGroups(new ArrayList<>())
                .build();

        group = ForumGroup.builder()
                .id("group")
                .groupName("group")
                .owner(owner)
                .description("description")
                .subscriptions(0)
                .subscribers(new ArrayList<>(){{
                    add(owner);
                    add(moderator);
                    add(subscriber);
                }})
                .moderators(new ArrayList<>() {{
                    add(owner);
                    add(moderator);
                }})
                .build();

        groupDto = GroupDto.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .subscriptions(group.getSubscriptions())
                .isSubscribed(true)
                .owner(group.getOwner().getUsername())
                .moderators(new ArrayList<>())
                .build();
    }

    @Test
    void addGroup() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.save(any())).thenReturn(group);
        when(groupMapper.mapToDto(any(), any())).thenReturn(groupDto);
        when(groupRepository.findById(anyString())).thenReturn(Optional.of(group));

        AddGroupRequest request = AddGroupRequest.builder()
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .build();

        GroupDto res = groupService.add("token", request);

        assertNotNull(res);
        assertEquals(groupDto, res);
    }

    @Test
    void getAllGroups() {

        List<GroupDto> groups = new ArrayList<>(){{add(groupDto);}};

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findAll(any())).thenReturn(new ArrayList<>(){{add(group);}});
        when(groupMapper.mapToDto(any(), any())).thenReturn(groupDto);

        List<GroupDto> res = groupService.getAll("token", 0, null);

        assertNotNull(res);
        assertEquals(groups, res);
    }

    @Test
    void getAllGroupsWithKeyword() {

        List<GroupDto> groups = new ArrayList<>(){{add(groupDto);}};

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findByGroupNameLike(any(), any())).thenReturn(new ArrayList<>(){{add(group);}});
        when(groupMapper.mapToDto(any(), any())).thenReturn(groupDto);

        List<GroupDto> res = groupService.getAll("token", 0, "keyword");

        assertNotNull(res);
        assertEquals(groups, res);
    }

    @Test
    void getCountGroups() {

        when(groupRepository.findAll()).thenReturn(new ArrayList<>(){{add(group);}});

        int res = groupService.getCount(null);

        assertEquals(1, res);
    }

    @Test
    void getGroup() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupMapper.mapToDto(any(), any())).thenReturn(groupDto);

        GroupDto res = groupService.get("token", group.getId());

        assertNotNull(res);
        assertEquals(groupDto, res);
    }

    @Test
    void editGroup() {

        EditGroupRequest request = EditGroupRequest.builder()
                .id(group.getId())
                .groupName("new group name")
                .description("new group description")
                .build();

        GroupDto editedGroup = GroupDto.builder()
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .build();

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupMapper.mapToDto(any(), any())).thenReturn(editedGroup);

        GroupDto res = groupService.edit("token", request);

        assertNotNull(res);
        assertEquals(request.getGroupName(), res.getGroupName());
        assertEquals(request.getDescription(), res.getDescription());
    }

    @Test
    void deleteGroupWrongUser() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(moderator));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        assertThrows(UnauthorizedException.class, () ->
                groupService.delete("token", group.getId()));
    }

    @Test
    void transferOwnershipGroup() {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userRepository.findByUsername(moderator.getUsername())).thenReturn(Optional.of(moderator));

        groupService.transferOwnership("token", group.getId(), moderator.getUsername());
    }

    @Test
    void getSubscribersFromGroup() {

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userMapper.mapToDto(any())).thenReturn(UserDto.builder().build());

        List<UserDto> res = groupService.getSubscribers(group.getId(), null);

        assertEquals(3, res.size());

    }

    @Test
    void subscribeGroup() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(nonSubscriber));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        groupService.subscribe("token", group.getId());

        ArgumentCaptor<ForumGroup> captor = ArgumentCaptor.forClass(ForumGroup.class);
        verify(groupRepository).save(captor.capture());

        ForumGroup captured = captor.getValue();
        assertEquals(group.getId(), captured.getId());
    }

    @Test
    void subscribeAlreadySubscribedGroup() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(subscriber));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        groupService.subscribe("token", group.getId());

        ArgumentCaptor<ForumGroup> captor = ArgumentCaptor.forClass(ForumGroup.class);
        verify(groupRepository).delete(captor.capture());

        ForumGroup captured = captor.getValue();
        assertEquals(group.getId(), captured.getId());
    }

    @Test
    void getModeratorsFromGroup() {
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userMapper.mapToDto(any())).thenReturn(UserDto.builder().build());

        List<UserDto> res = groupService.getModerators(group.getId());

        assertEquals(2, res.size());
    }

    @Test
    void isModeratorInGroupTrue() {
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userRepository.findByUsername(moderator.getUsername())).thenReturn(Optional.of(moderator));

        boolean res = groupService.isModerator(moderator.getUsername(), group.getId());

        assertTrue(res);
    }

    @Test
    void isModeratorInGroupFalse() {
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userRepository.findByUsername(subscriber.getUsername())).thenReturn(Optional.of(subscriber));

        boolean res = groupService.isModerator(subscriber.getUsername(), group.getId());

        assertFalse(res);
    }

    @Test
    void addModeratorToGroup() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userRepository.findByUsername(subscriber.getUsername())).thenReturn(Optional.of(subscriber));

        groupService.addModerator("token", subscriber.getUsername(), group.getId());

        ArgumentCaptor<ForumGroup> captor = ArgumentCaptor.forClass(ForumGroup.class);
        verify(groupRepository).save(captor.capture());

        ForumGroup captured = captor.getValue();
        assertTrue(captured.getModerators().contains(subscriber));
    }

    @Test
    void deleteModeratorFromGroup() {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(owner));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userRepository.findByUsername(moderator.getUsername())).thenReturn(Optional.of(moderator));

        groupService.addModerator("token", moderator.getUsername(), group.getId());

        ArgumentCaptor<ForumGroup> captor = ArgumentCaptor.forClass(ForumGroup.class);
        verify(groupRepository).save(captor.capture());

        ForumGroup captured = captor.getValue();
        assertTrue(captured.getModerators().contains(moderator));
    }

    @Test
    void isGroupNameTakenOk() {
        when(groupRepository.findByGroupName(group.getGroupName())).thenReturn(Optional.empty());

        assertFalse(groupService.isGroupNameTaken(group.getGroupName()));
    }

    @Test
    void isGroupNameTakenNok() {
        when(groupRepository.findByGroupName(group.getGroupName())).thenReturn(Optional.of(group));

        assertTrue(groupService.isGroupNameTaken(group.getGroupName()));
    }
}