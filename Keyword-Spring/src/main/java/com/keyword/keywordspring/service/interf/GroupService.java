package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;

import java.util.List;

public interface GroupService {

    void createGroup(AppUser user, CreateGroupRequest request);


    List<GroupDto> getGroups(Integer page, String name, AppUser user);
    Integer getGroupsCount(String name);
    GroupDto getGroup(String id, AppUser user);

    void editGroup(AppUser user, EditGroupRequest request);
    void deleteGroup(AppUser user, String groupId);

    List<UserDto> getSubscribers(String groupId, String username);

    void addModerator(AppUser owner, String moderatorUsername, String groupId);
    void removeModerator(AppUser owner, String moderatorUsername, String groupId);
    List<UserDto> getModerators(String groupId);
    boolean isModerator(String username, String groupId);

    boolean isGroupNameTaken(String name);

    void subscribeGroup(AppUser user, String groupId);
    List<GroupDto> getSubscribedGroups(AppUser user);
}
