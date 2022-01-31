package com.keyword.keywordspring.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;
    private Date dateCreated;
    private int posts;
    private int comments;
    private String avatarUrl;
}
