package com.joblens.resume;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "resumes")
@Getter @Setter @NoArgsConstructor
public class ResumeDocument {
    @Id private String id;
    private String userEmail;
    private String originalFilename;
    private String contentType;
    private long fileSize;
    private String extractedText;
    private ExtractedProfile extractedProfile;
    private String processingStatus;
    private String processingError;
    private byte[] originalFile;
    private Instant uploadedAt;
}
