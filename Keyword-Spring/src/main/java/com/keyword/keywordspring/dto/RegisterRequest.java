package com.keyword.keywordspring.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
