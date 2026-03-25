package com.fms.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {

    public static <T> ResponseEntity<SuccessResponse<T>> success(String msg, T data) {
        return ResponseEntity.ok(new SuccessResponse<>(msg, data));
    }
    
    public static <T> ResponseEntity<SuccessResponse<T>> created(String msg, T data) {
        return new ResponseEntity<SuccessResponse<T>>(new SuccessResponse<>(msg, data), HttpStatus.CREATED);
    }


    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static ResponseEntity<ErrorResponse> failure(
            String code, String message, String details, HttpStatus status) {

        ErrorDetails errorDetails = new ErrorDetails(code, message, details);
        return new ResponseEntity<>(new ErrorResponse(errorDetails), status);
    }
}

