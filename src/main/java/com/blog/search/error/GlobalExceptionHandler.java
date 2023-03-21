package com.blog.search.error;

import com.blog.search.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value=ApiException.class)
    protected ResponseEntity<ErrorResponse> handlerArgumentException(ApiException e) {
        log.error(e.getMessage(),e);
        ErrorResponse errorResponse = ErrorResponse.builder().httpStatus(e.getStatusCode().getStatus()).code(e.getStatusCode().getStatus().value()).message(e.getStatusCode().getMessage()).build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> ErrorHandler(MissingServletRequestParameterException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "파라미터 명을 확인해 주세요.");
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
