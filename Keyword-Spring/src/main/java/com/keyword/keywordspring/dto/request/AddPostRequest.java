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
public class AddPostRequest {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String groupId;
}
