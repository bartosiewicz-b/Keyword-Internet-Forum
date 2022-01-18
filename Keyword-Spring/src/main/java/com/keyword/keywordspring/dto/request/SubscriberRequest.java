package com.keyword.keywordspring.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SubscriberRequest {

    @NotNull
    private String groupId;

    private String username;
}
