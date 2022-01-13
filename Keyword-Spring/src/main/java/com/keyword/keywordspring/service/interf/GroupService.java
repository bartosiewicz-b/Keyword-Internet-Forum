package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.model.AppUser;

import java.util.List;

public interface GroupService {

    void createGroup(AppUser user, CreateGroupRequest request);

    List<GroupDto> getGroups(Integer page, String name, AppUser user);

    Integer getGroupsCount(String name);

    GroupDto getGroup(String id, AppUser user);

    void editGroup(AppUser user, EditGroupRequest request);

    boolean isGroupNameTaken(String name);

    void subscribeGroup(AppUser user, String groupId);

    List<GroupDto> getSubscribedGroups(AppUser user);

    void deleteGroup(AppUser user, String groupId);
}
