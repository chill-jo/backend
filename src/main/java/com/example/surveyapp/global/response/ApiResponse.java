package com.example.surveyapp.global.response;

import java.time.LocalDateTime;

public class ApiResponse<T>{

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true,message,data);
    }

    public static <T> ApiResponse<T> fail(String message,T errorDate) {
        return new ApiResponse<>(false,message,errorDate);
    }

}
