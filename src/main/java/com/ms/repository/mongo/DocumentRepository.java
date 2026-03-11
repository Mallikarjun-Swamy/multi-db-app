package com.ms.repository.mongo;

import com.ms.model.mongo.DocumentFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentFile,String> {
}
