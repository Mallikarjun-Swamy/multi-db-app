package com.ms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;

    private String name;

    private Double price;
}