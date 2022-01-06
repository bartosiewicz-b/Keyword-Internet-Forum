package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.exception.UnauthorizedException;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
import com.keyword.keywordspring.exception.GroupDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.GroupSubscription;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.repository.GroupSubscriptionRepository;
import com.keyword.keywordspring.service.interf.GroupService;
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
    private final GroupMapper groupMapper;

    @Override
    public void createGroup(AppUser user, CreateGroupRequest request) {

        ForumGroup forumGroup = ForumGroup.builder()
                .id(request.getGroupName().toLowerCase(Locale.ROOT).replace(' ', '-'))
                .owner(user)
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .dateCreated(new Date(System.currentTimeMillis()))
                .subscriptions(0)
                .build();

        groupRepository.save(forumGroup);

        subscribeGroup(user, forumGroup.getId());
    }

    @Override
    public List<GroupDto> getGroups(Integer page, String name, AppUser user) {

        return (name == null ?
                groupRepository.findAll(PageRequest.of(page, 10, Sort.by("subscriptions").descending())) :
                groupRepository.findByGroupNameLike("%"+name+"%", PageRequest.of(page, 10, Sort.by("subscriptions").descending())))
                        .stream()
                        .map(res -> groupMapper.mapToDto(res, user))
                        .collect(Collectors.toList());
    }

    @Override
    public GroupDto getGroup(String id, AppUser user) {
        ForumGroup group = groupRepository.findById(id).orElseThrow(() -> new GroupDoesNotExistException(id));

        return groupMapper.mapToDto(group, user);
    }

    @Override
    public void editGroup(AppUser user, EditGroupRequest request) {
        if(groupRepository.findById(request.getId()).isEmpty() ||
                !Objects.equals(user.getId(), groupRepository.findById(request.getId()).get().getOwner().getId()))
            throw new UnauthorizedException();

        ForumGroup group = groupRepository.findById(request.getId())
                .orElseThrow(() -> new GroupDoesNotExistException(request.getId()));

        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());

        groupRepository.save(group);
    }

    @Override
    public boolean isGroupNameTaken(String name) {
        return groupRepository.findByGroupName(name).isPresent();
    }

    @Override
    public void subscribeGroup(AppUser user, String groupId) {
        ForumGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupDoesNotExistException(groupId));

        Optional<GroupSubscription> subscription = groupSubscriptionRepository.findByUserAndGroup(user, group);

        if(subscription.isEmpty()) {
            groupSubscriptionRepository.save(GroupSubscription.builder()
                    .group(group)
                    .user(user)
                    .build());

            group.setSubscriptions(group.getSubscriptions() + 1);
            groupRepository.save(group);
        }
        else {
            groupSubscriptionRepository.delete(subscription.get());

            group.setSubscriptions(group.getSubscriptions() - 1);
            groupRepository.save(group);
        }
    }

    @Override
    public void deleteGroup(AppUser user, String groupId) {
        if(groupRepository.findById(groupId).isEmpty() ||
                !Objects.equals(user.getId(), groupRepository.findById(groupId).get().getOwner().getId()))
            throw new UnauthorizedException();

        groupRepository.deleteById(groupId);
    }
}
