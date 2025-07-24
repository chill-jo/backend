package com.example.surveyapp.domain.user.controller;

import com.example.surveyapp.domain.user.controller.dto.UserRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserResponseDto;
import com.example.surveyapp.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.surveyapp.domain.user.config.UserFixtureGenerator.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void User_조회_API를_호출하면_회원이_조회된다() throws Exception {
        // Given
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(ID)
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .userRole(ROLE)
                .build();

        when(userService.getMyInfo(ID)).thenReturn(responseDto);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/my-page")
                .param("userId", ID.toString())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        verify(userService, times(1)).getMyInfo(ID);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.name").value(NAME))
                .andExpect(jsonPath("$.data.nickname").value(NICKNAME))
                .andExpect(jsonPath("$.data.role").value(ROLE.name()));
    }

    @Test
    public void User_수정_API를_호출하면_회원정보가_수정된다() throws Exception{
        // Given
        UserRequestDto dto = new UserRequestDto("newEmail@example.com", "newPassword", "newName", "newNickname");

        UserResponseDto updatedDto = UserResponseDto.builder()
                .id(ID)
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .userRole(ROLE)
                .build();

        when(userService.updateMyInfo(eq(ID), any(UserRequestDto.class))).thenReturn(updatedDto);

        // When
        ResultActions resultActions = mockMvc.perform(patch("/api/my-page")
                .param("userId", ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // Then
        verify(userService, times(1)).updateMyInfo(eq(ID), any(UserRequestDto.class));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.data.name").value(dto.getName()))
                .andExpect(jsonPath("$.data.nickname").value(dto.getNickname()))
                .andExpect(jsonPath("$.data.role").value(ROLE.name()));
    }
}
