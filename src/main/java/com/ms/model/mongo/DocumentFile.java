package com.ms.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "my_mongo_db")
@Data
public class DocumentFile {

    @Id
    private String id;

    private String fileName;

    private String filePath;
}