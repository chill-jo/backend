package com.example.surveyapp.global.response.exception;

import com.example.surveyapp.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode, e.getMessage());

        log.warn("[클라이언트 예외 발생] {} - {}", errorCode.name(), e.getMessage());

        return ResponseEntity
                .status(errorResponseDto.getStatus())
                .body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        //요청 필드 에러 메시지 리스트
        List errorMessages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        //모든 메시지 하나로 합침
        String errorMessage = String.join(", ", errorMessages);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
            httpStatus,
            errorMessage
        );

        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodValidationException(HandlerMethodValidationException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                httpStatus,
                "Parameter 값이 잘못되었습니다."
        );

        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnknownException(Exception e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("[예외 발생] : ", e);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                httpStatus,
                e.getMessage()
        );

        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }


}
