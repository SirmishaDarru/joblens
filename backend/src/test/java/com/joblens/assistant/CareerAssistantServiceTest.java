package com.joblens.assistant;

import com.joblens.resume.ResumeDocument;
import com.joblens.resume.ResumeRepository;
import com.joblens.job.JobRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CareerAssistantServiceTest {
    @Test void refusesUnsupportedQuestions() {
        ResumeRepository resumes=mock(ResumeRepository.class); JobRepository jobs=mock(JobRepository.class); ResumeDocument r=new ResumeDocument(); r.setExtractedText("Java backend project"); when(resumes.findFirstByUserEmailOrderByUploadedAtDesc("a@b.com")).thenReturn(Optional.of(r));
        AssistantResponse response=new CareerAssistantService(resumes,jobs,mock(OpenAiClient.class)).answer("a@b.com",new AssistantRequest("What is the weather?",null));
        assertFalse(response.grounded()); assertTrue(response.citations().isEmpty());
    }
}
