package com.ms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentResponseDto {

    private String id;

    private String fileName;

    private String filePath;
}
