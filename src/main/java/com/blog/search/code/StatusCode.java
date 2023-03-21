package com.blog.search.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {
    OK(HttpStatus.OK, "정상 처리되었습니다."),
    NO_DATA(HttpStatus.NO_CONTENT, "검색 내용이 없습니다."),
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "검색어를 확인해주세요."),

    //404 NOT_FOUND 잘못된 리소스 접근
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 URL입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 서버 팀에 연락주세요!");

    private final HttpStatus status;
    private final String message;
}
