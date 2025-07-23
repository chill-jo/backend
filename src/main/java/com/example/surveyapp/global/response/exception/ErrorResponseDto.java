package com.example.surveyapp.global.response.exception;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

}
