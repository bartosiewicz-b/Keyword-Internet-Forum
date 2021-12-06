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
public class ChangeUsernameRequest {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String newUsername;
}
