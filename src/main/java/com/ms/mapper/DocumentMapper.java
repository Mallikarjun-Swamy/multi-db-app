package com.ms.mapper;

import com.ms.dto.request.DocumentRequestDto;
import com.ms.dto.response.DocumentResponseDto;
import com.ms.model.mongo.DocumentFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", implementationName = "DocumentMapperImpl")
public interface DocumentMapper {

    DocumentFile toEntity(DocumentRequestDto dto);

    DocumentResponseDto toDto(DocumentFile file);

    List<DocumentResponseDto> toDtoList(List<DocumentFile> list);

}