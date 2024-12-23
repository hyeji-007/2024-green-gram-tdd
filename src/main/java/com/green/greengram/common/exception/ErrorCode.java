package com.green.greengram.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public interface ErrorCode {
    //나를 상속박은 ENUM은 String message 멤버필드를 꼭 가져야 함
    String name();
    String getMessage();
    HttpStatus getHttpStatus(); //응답 코드 결정
}
