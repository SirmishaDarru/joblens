package com.joblens.matching;

import com.joblens.job.Job;
import com.joblens.resume.ResumeDocument;
import com.joblens.resume.ResumeRepository;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchingServiceTest {
    @Test void reportsMatchedAndMissingSkillsWithEvidence() {
        ResumeRepository repository = mock(ResumeRepository.class);
        ResumeDocument resume = new ResumeDocument(); resume.setUserEmail("candidate@example.com"); resume.setExtractedText("Backend Engineer\nBuilt Java and REST APIs for a student platform."); resume.setUploadedAt(Instant.now());
        when(repository.findFirstByUserEmailOrderByUploadedAtDesc("candidate@example.com")).thenReturn(Optional.of(resume));
        Job job = new Job(); job.setId(1L); job.setTitle("Backend Engineer"); job.setRequiredSkills("Java, REST APIs, SQL"); job.setDescription("Build reliable services using Java and SQL.");
        MatchResult result = new MatchingService(repository).match("candidate@example.com", job);
        assertTrue(result.matchedSkills().contains("java")); assertTrue(result.missingSkills().contains("sql")); assertFalse(result.evidence().isEmpty());
    }
}
