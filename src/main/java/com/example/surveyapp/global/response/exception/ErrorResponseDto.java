package com.example.surveyapp.global.response.exception;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorResponseDto {

    private final int status;
    private final String errorCode;
    private final String message;

    public ErrorResponseDto(ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus().value();
        this.errorCode = errorCode.name();
        this.message = message;
    }
    public ErrorResponseDto(HttpStatus httpStatus, String message){
        this.status = httpStatus.value();
        this.errorCode = httpStatus.getReasonPhrase();
        this.message = message;
    }
}
