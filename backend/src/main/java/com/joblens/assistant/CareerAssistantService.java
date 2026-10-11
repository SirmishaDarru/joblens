package com.joblens.assistant;

import com.joblens.job.Job;
import com.joblens.job.JobRepository;
import com.joblens.resume.ResumeDocument;
import com.joblens.resume.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class CareerAssistantService {
    private final ResumeRepository resumes;
    private final JobRepository jobs;
    private final OpenAiClient openAiClient;

    public AssistantResponse answer(String email, AssistantRequest request) {
        ResumeDocument resume = resumes.findFirstByUserEmailOrderByUploadedAtDesc(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload a resume before using the assistant"));
        Job job = request.jobId() == null ? null : jobs.findById(request.jobId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        String question = request.question().toLowerCase(Locale.ROOT);
        String text = resume.getExtractedText();
        List<String> citations = new ArrayList<>();
        String context = "RESUME:\n" + text + (job == null ? "" : "\n\nJOB:\n" + job.getTitle() + "\n" + job.getRequiredSkills() + "\n" + job.getDescription());
        if (!question.contains("ignore previous") && !question.contains("reveal system") && !question.contains("system prompt")) {
            Optional<String> generated = openAiClient.answer(context, request.question());
            if (generated.isPresent()) return new AssistantResponse(generated.get(), List.of("Uploaded resume", job == null ? "" : "Selected job description"), true, "Generated from the supplied context; hiring outcomes are not predicted.");
        }
        if (question.contains("skill") || question.contains("match") || question.contains("qualified")) {
            String skills = resume.getExtractedProfile() == null ? "the uploaded resume text" : String.join(", ", resume.getExtractedProfile().skills());
            if (job == null) return new AssistantResponse("Your resume shows evidence for: " + skills + ". Select a job if you want a role-specific comparison.", List.of("Resume profile: detected skills"), true, "This answer is based only on extracted resume content.");
            String required = job.getRequiredSkills(); String lower = text.toLowerCase(Locale.ROOT); List<String> matched = Arrays.stream(required.split(",")).map(String::trim).filter(s -> lower.contains(s.toLowerCase(Locale.ROOT))).toList();
            citations.add("Job requirements: " + required); citations.addAll(evidence(text, matched));
            return new AssistantResponse("For " + job.getTitle() + ", the resume contains evidence for " + (matched.isEmpty() ? "none of the listed required skills" : String.join(", ", matched)) + ". Skills not found in the resume should be treated as unsubstantiated, not proof that you do not have them.", citations, true, "The assistant does not predict hiring outcomes.");
        }
        if (question.contains("project") || question.contains("experience") || question.contains("evidence")) {
            String excerpt = text.length() > 500 ? text.substring(0, 500) + "…" : text; return new AssistantResponse("The most relevant evidence available in your uploaded resume is shown below. Use it as the basis for your application response.", List.of("Resume excerpt: " + excerpt), true, "Only uploaded resume text was used.");
        }
        return new AssistantResponse("I can help compare your resume with a job, identify evidenced skills, and locate relevant experience. Ask a question about a selected job or resume evidence.", List.of(), false, "The available sources do not support a grounded answer to that question.");
    }

    private List<String> evidence(String text, List<String> terms) { String lower=text.toLowerCase(Locale.ROOT); return terms.stream().map(t->{int i=lower.indexOf(t.toLowerCase(Locale.ROOT));if(i<0)return null;int s=Math.max(0,text.lastIndexOf('\n',i)+1),e=text.indexOf('\n',i);return "Resume evidence: "+text.substring(s,e<0?Math.min(text.length(),s+220):e).trim();}).filter(Objects::nonNull).collect(Collectors.toList()); }
}
