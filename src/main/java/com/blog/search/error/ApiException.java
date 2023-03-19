package com.blog.search.error;

import com.blog.search.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
