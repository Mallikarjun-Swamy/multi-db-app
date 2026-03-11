package com.ms.dto.response;

import com.ms.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseUtil {

    // Generic success response
    public static <T> ResponseEntity<ApiResponse<T>> success(
            HttpStatus status,
            String message,
            T data) {

        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true) // request successful
                .message(message) // success message
                .data(data) // response payload
                .timestamp(LocalDateTime.now()) // response time
                .build();

        return ResponseEntity.status(status).body(response);
    }

    // Generic error response
    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus status,
            String message) {

        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(false) // request failed
                .message(message) // error message
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
