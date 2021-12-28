package com.keyword.keywordspring.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupDto {

    private String id;
    private String groupName;
    private String description;
    private Integer subscriptions;
    private Boolean isSubscribed;
    private String owner;
}
