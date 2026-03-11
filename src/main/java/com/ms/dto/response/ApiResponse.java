package com.ms.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    // Indicates request success or failure
    private boolean success;

    // Message describing the result
    private String message;

    // Actual response data
    private T data;

    // Time when response is generated
    private LocalDateTime timestamp;
}
