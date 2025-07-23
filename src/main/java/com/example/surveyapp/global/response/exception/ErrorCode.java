package com.example.surveyapp.global.response.exception;

import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통 에러 정의
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생하였습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하는 HTTP 메서드가 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),



    NOT_ADMIN_USER_ERROR(HttpStatus.UNAUTHORIZED,"관리자 계정으로 로그인하세요."),


    //설문 에러
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 설문입니다."),
    SURVEY_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 설문입니다."),
    SURVEY_CANNOT_BE_DELETED(HttpStatus.CONFLICT, "현재 설문 상태에서는 삭제할 수 없습니다."),
    INVALID_SURVEY_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "설문 상태를 변경할 수 없습니다."),
    SURVEY_CANNOT_BE_MODIFIED(HttpStatus.CONFLICT, "설문 상세정보를 수정할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }



}
