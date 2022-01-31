package com.keyword.keywordspring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditGroupRequest {

    @NotNull
    private String id;
    @NotNull
    private String groupName;
    @NotNull
    private String description;

    private String avatarUrl;
}
