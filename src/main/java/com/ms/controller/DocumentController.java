package com.ms.controller;

import com.ms.dto.request.DocumentRequestDto;
import com.ms.dto.response.ApiResponse;
import com.ms.dto.response.DocumentResponseDto;
import com.ms.dto.response.ResponseUtil;
import com.ms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // UPLOAD DOCUMENT
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> uploadDocument(
            @RequestBody DocumentRequestDto request) {

        DocumentResponseDto document = documentService.uploadDocument(request);

        return ResponseUtil.success(
                HttpStatus.CREATED,
                "Document uploaded successfully",
                document
        );
    }

    // GET ALL DOCUMENTS
    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getAllDocuments() {

        List<DocumentResponseDto> documents = documentService.getAllDocuments();

        return ResponseUtil.success(
                HttpStatus.OK,
                "Documents fetched successfully",
                documents
        );
    }

    // GET DOCUMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> getDocumentById(
            @PathVariable String id) {

        DocumentResponseDto document =
                documentService.getDocumentById(id);

        return ResponseUtil.success(
                HttpStatus.OK,
                "Document fetched successfully",
                document
        );
    }

    // UPDATE DOCUMENT
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> updateDocument(
            @PathVariable String id,
            @RequestBody DocumentRequestDto request) {

        DocumentResponseDto updated =
                documentService.updateDocument(id, request);

        return ResponseUtil.success(
                HttpStatus.OK,
                "Document updated successfully",
                updated
        );
    }

    // DELETE DOCUMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @PathVariable String id) {

        documentService.deleteDocument(id);

        return ResponseUtil.success(
                HttpStatus.OK,
                "Document deleted successfully",
                null
        );
    }

}