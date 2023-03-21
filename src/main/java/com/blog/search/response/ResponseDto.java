package com.blog.search.response;

import com.blog.search.code.StatusCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private StatusCode status;
    private String message;
    private Object data;
}
