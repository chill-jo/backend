package com.example.surveyapp.global.response.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String message;

  //에러코드 enum에 정의된 메시지 그대로 사용
  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }

  //커스텀 메시지 직접 지정 가능
  public CustomException(ErrorCode errorCode, String message){
    super(message);
    this.errorCode = errorCode;
    this.message = message;
  }
}
