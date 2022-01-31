package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.AddGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.GroupDoesNotExistException;
import com.keyword.keywordspring.exception.UserDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.mapper.interf.UserMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.GroupSubscription;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.repository.GroupSubscriptionRepository;
import com.keyword.keywordspring.repository.UserRepository;
import com.keyword.keywordspring.service.interf.GroupService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupSubscriptionRepository groupSubscriptionRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    @Override
    public GroupDto add(String token, AddGroupRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.save(ForumGroup.builder()
                .id(request.getGroupName().toLowerCase(Locale.ROOT).trim().replace(' ', '-'))
                .owner(user)
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .dateCreated(new Date(System.currentTimeMillis()))
                .subscriptions(0)
                .subscribers(new ArrayList<>())
                .moderators(new ArrayList<>(){{add(user);}})
                .build());

        subscribe(token, group.getId());

        return groupMapper.mapToDto(group, user);
    }

    @Override
    public List<GroupDto> getAll(String token, Integer page, String keyword) {

        AppUser user = jwtUtil.getUserFromToken(token).orElse(null);

        List<ForumGroup> groups = keyword == null ?
                groupRepository.findAll(PageRequest.of(page, 10, Sort.by("subscriptions").descending())) :
                groupRepository.findByGroupNameLike("%"+keyword+"%", PageRequest.of(page, 10, Sort.by("subscriptions").descending()));

        return (groups.stream()
                .map(res -> groupMapper.mapToDto(res, user))
                .collect(Collectors.toList()));
    }

    @Override
    public int getCount(String keyword) {

        return keyword == null ?
                groupRepository.findAll().size() :
                groupRepository.findByGroupNameLike("%"+keyword+"%").size();
    }

    @Override
    public GroupDto get(String token, String id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElse(null);

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        return groupMapper.mapToDto(group, user);
    }

    @Override
    public GroupDto edit(String token, EditGroupRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.findById(request.getId())
                .orElseThrow(() -> new GroupDoesNotExistException(request.getId()));

        if(!Objects.equals(user, group.getOwner()))
            throw new UnauthorizedException(user, group);

        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());
        group.setAvatarUrl(request.getAvatarUrl());

        groupRepository.save(group);

        return groupMapper.mapToDto(group, user);
    }

    @Override
    public void delete(String token, String id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        if(!Objects.equals(user, group.getOwner()))
            throw new UnauthorizedException(user, group);

        groupRepository.deleteById(id);
    }

    @Override
    public void transferOwnership(String token, String id, String newOwnerUsername) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        AppUser newOwner = userRepository.findByUsername(newOwnerUsername)
                .orElseThrow(() -> new UserDoesNotExistException(newOwnerUsername));

        if(newOwner.equals(user)) return;

        user.getOwnedGroups().remove(group);
        group.setOwner(newOwner);
        newOwner.getOwnedGroups().add(group);

        userRepository.save(user);
        userRepository.save(newOwner);
        groupRepository.save(group);
    }

    @Override
    public List<UserDto> getSubscribers(String id, String keyword) {
        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        group.getSubscribers().removeIf(obj ->
                (keyword != null && !obj.getUsername().contains(keyword)) ||
                        obj.getModeratedGroups().contains(group)
        );

        return group.getSubscribers().stream().limit(10)
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void subscribe(String token, String id) {

        AppUser user = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        Optional<GroupSubscription> subscription = groupSubscriptionRepository.findByUserAndGroup(user, group);

        if(subscription.isEmpty()) {
            groupSubscriptionRepository.save(GroupSubscription.builder()
                    .group(group)
                    .user(user)
                    .build());

            group.setSubscriptions(group.getSubscriptions() + 1);
            group.getSubscribers().add(user);
            user.getSubscribed().add(group);
        }
        else {
            groupSubscriptionRepository.delete(subscription.get());

            group.setSubscriptions(group.getSubscriptions() - 1);

            if(group.getSubscriptions()<0)
                group.setSubscriptions(0);

            group.getSubscribers().remove(user);
            user.getSubscribed().remove(group);
        }

        groupRepository.save(group);
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getModerators(String id) {

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        return group.getModerators().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isModerator(String username, String id) {

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UserDoesNotExistException(username));

        return group.getModerators().contains(user);
    }

    @Override
    public void addModerator(String token, String moderatorUsername, String id) {

        AppUser owner = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        AppUser moderator = userRepository.findByUsername(moderatorUsername)
                .orElseThrow(() -> new UserDoesNotExistException(moderatorUsername));

        if(!group.getOwner().equals(owner)) throw new UnauthorizedException(owner, group);

        group.getModerators().add(moderator);
        moderator.getModeratedGroups().add(group);

        groupRepository.save(group);
        userRepository.save(moderator);
    }

    @Override
    public void deleteModerator(String token, String moderatorUsername, String id) {

        AppUser owner = jwtUtil.getUserFromToken(token).orElseThrow(UnauthorizedException::new);

        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        AppUser moderator = userRepository.findByUsername(moderatorUsername)
                .orElseThrow(() -> new UserDoesNotExistException(moderatorUsername));

        if(!group.getOwner().equals(owner)) throw new UnauthorizedException(owner, group);

        group.getModerators().remove(moderator);
        moderator.getModeratedGroups().remove(group);

        groupRepository.save(group);
        userRepository.save(moderator);
    }

    @Override
    public boolean isGroupNameTaken(String name) {

        return groupRepository.findByGroupName(name).isPresent();
    }
}
