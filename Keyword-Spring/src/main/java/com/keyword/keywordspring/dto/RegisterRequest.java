package com.keyword.keywordspring.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class RegisterRequest {

    @NotNull
    @Size(min = 4, max = 32, message = "Username length is not valid.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can contain only letters and numbers.")
    private String username;

    @NotNull
    @Email(message = "Not a valid email.")
    private String email;

    @NotNull
    @Size(min = 8, message = "Password is too short.")
    private String password;
}
