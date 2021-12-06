package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.request.ChangeEmailRequest;
import com.keyword.keywordspring.dto.request.ChangeUsernameRequest;
import com.keyword.keywordspring.dto.request.LoginRequest;
import com.keyword.keywordspring.dto.response.LoginResponse;
import com.keyword.keywordspring.dto.request.RegisterRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ReturnValue;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        when(userService.login(any())).thenReturn(new ReturnValue<>(user));

        String request = mapper.writeValueAsString(LoginRequest.builder()
                .login(user.getUsername())
                .password(user.getPassword())
                .build());

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

        LoginResponse response = LoginResponse.builder()
                .token("token")
                .refreshToken("refreshToken")
                .build();

        when(jwtUtil.refreshJwt(anyString())).thenReturn(new ReturnValue<>(response));

        MvcResult result = mockMvc.perform(get("/auth/refresh/token")
                .header("refresh", "refreshToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(response), result.getResponse().getContentAsString());
    }

    @Test
    void changeUsername() throws Exception {

        String request = mapper.writeValueAsString(ChangeUsernameRequest.builder()
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .newUsername("new username")
                .build());

        mockMvc.perform(post("/auth/change/username")
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
    void changeEmail() throws Exception {

        String request = mapper.writeValueAsString(ChangeEmailRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .newEmail("newEmail@email.com")
                .build());

        mockMvc.perform(post("/auth/change/email")
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

        when(userService.isUsernameTaken(anyString())).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/auth/validate-new/username")
                .content(user.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                        .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "true");
    }

    @Test
    void validateNewEmail() throws Exception {

        when(userService.isEmailTaken(anyString())).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/auth/validate-new/email")
                .content(user.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "true");
    }
}