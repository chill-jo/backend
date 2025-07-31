package com.example.surveyapp.global.security.handler;

import com.example.surveyapp.global.response.ApiResponse;
import com.example.surveyapp.global.response.exception.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        writeErrorResponse(response, errorResponse);
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorResponseDto error) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), ApiResponse.fail(error.getMessage(), error));
    }
}