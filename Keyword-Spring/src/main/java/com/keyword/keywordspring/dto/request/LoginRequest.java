package com.keyword.keywordspring.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LoginRequest {

    @NotNull
    private String login;

    @NotNull
    private String password;
}
