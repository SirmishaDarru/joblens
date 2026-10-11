package com.joblens.resume;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ResumeRepository extends MongoRepository<ResumeDocument, String> {
    Optional<ResumeDocument> findFirstByUserEmailOrderByUploadedAtDesc(String userEmail);
    Optional<ResumeDocument> findByIdAndUserEmail(String id, String userEmail);
}
