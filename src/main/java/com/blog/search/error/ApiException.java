package com.blog.search.error;

import com.blog.search.code.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException extends RuntimeException{
    private final StatusCode statusCode;
}
