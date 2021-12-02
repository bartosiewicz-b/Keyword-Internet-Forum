package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.model.AppUser;

public interface GroupService {

    void createGroup(AppUser user, CreateGroupRequest request);

    boolean isGroupNameTaken(String name);
}
