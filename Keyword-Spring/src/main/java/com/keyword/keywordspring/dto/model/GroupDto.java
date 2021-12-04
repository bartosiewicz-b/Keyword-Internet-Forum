package com.keyword.keywordspring.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupDto {

    private Long id;
    private String groupName;
    private String description;
}
