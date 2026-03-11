package com.ms.service;

import com.ms.dto.request.DocumentRequestDto;
import com.ms.dto.response.DocumentResponseDto;
import com.ms.mapper.DocumentMapper;
import com.ms.model.mongo.DocumentFile;
import com.ms.repository.mongo.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;
    private final DocumentMapper mapper;

    public DocumentResponseDto uploadDocument(DocumentRequestDto dto){
        DocumentFile file = mapper.toEntity(dto);
        return mapper.toDto(repository.save(file));
    }

    public List<DocumentResponseDto> getAllDocuments(){
        return mapper.toDtoList(repository.findAll());
    }

    public DocumentResponseDto getDocumentById(String id){
        DocumentFile file = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not Found with id " + id));

        return mapper.toDto(file);
    }

    public DocumentResponseDto updateDocument(String id,DocumentRequestDto dto){
        DocumentFile file = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not Found with id " + id));

        file.setFileName(dto.getFileName());
        file.setFilePath(dto.getFilePath());

        return mapper.toDto(repository.save(file));
    }

    public void deleteDocument(String id){
        repository.deleteById(id);
    }

}
