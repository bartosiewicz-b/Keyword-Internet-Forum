package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangePasswordRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.response.TokenResponse;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class AuthApiTest {

    @MockBean
    UserService userService;

    @MockBean
    JwtUtil jwtUtil;


    MockMvc mockMvc;
    ObjectMapper mapper;

    AppUser user;

    TokenResponse tokenResponse;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocs,
               WebApplicationContext context) {

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocs))
                .build();

        user = AppUser.builder()
                .email("testUser1@email.com")
                .username("testUser1")
                .password("password")
                .build();

        tokenResponse = TokenResponse.builder()
                .token("token")
                .refreshToken("refreshToken")
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Test
    void register() throws Exception {
        String request = mapper.writeValueAsString(RegisterRequest.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build());

        mockMvc.perform(post("/auth/register")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void login() throws Exception {

        String request = mapper.writeValueAsString(LoginRequest.builder()
                .login(user.getUsername())
                .password(user.getPassword())
                .build());

        when(userService.login(any())).thenReturn(user);
        when(jwtUtil.generateTokenResponse(user)).thenReturn(tokenResponse);

        mockMvc.perform(post("/auth/login")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void refreshToken() throws Exception {

        when(jwtUtil.refreshJwt(anyString())).thenReturn(tokenResponse);

        MvcResult result = mockMvc.perform(post("/auth/refresh-token")
                .content("refreshToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(tokenResponse), result.getResponse().getContentAsString());
    }

    @Test
    void changeAvatar() throws Exception {

        mockMvc.perform(post("/auth/change-avatar")
                        .header("Authorization", "token")
                        .content("newAvatarUrl")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void changeUsername() throws Exception {

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/change-username")
                        .header("Authorization", "token")
                        .content("newUsername")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void changeEmail() throws Exception {

        String request = mapper.writeValueAsString(ChangeEmailRequest.builder()
                .password(user.getPassword())
                .newEmail("newEmail@email.com")
                .build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/change-email")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
    }

    @Test
    void changePassword() throws Exception {

        String request = mapper.writeValueAsString(ChangePasswordRequest.builder()
                .oldPassword(user.getPassword())
                .newPassword("newPassword")
                .build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/change-password")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
    }

    @Test
    void validateNewUsername() throws Exception {

        when(userService.validateNewUsername(anyString())).thenReturn(true);

        mockMvc.perform(post("/auth/validate-new-username")
                .content("newUsername")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void validateNewEmail() throws Exception {

        when(userService.validateNewEmail(anyString())).thenReturn(true);

        mockMvc.perform(post("/auth/validate-new-email")
                .content("newEmail@email.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}