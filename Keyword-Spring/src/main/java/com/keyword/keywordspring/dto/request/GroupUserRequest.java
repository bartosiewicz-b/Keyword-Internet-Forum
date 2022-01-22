package com.keyword.keywordspring.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class GroupUserRequest {

    @NotNull
    private String groupId;
    @NotNull
    private String username;
}
