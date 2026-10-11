package com.joblens.matching;

import com.joblens.job.Job;
import com.joblens.resume.ResumeDocument;
import com.joblens.resume.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MatchingService {
    private static final List<String> SKILL_CATALOG = List.of(
            "java", "python", "javascript", "typescript", "c++", "sql", "mysql", "postgresql", "mongodb", "redis", "spring boot", "spring", "rest apis", "rest", "microservices", "react", "angular", "node.js", "docker", "kubernetes", "aws", "azure", "git", "linux", "system design", "data structures", "algorithms", "machine learning", "natural language processing", "tensorflow", "pytorch", "multithreading", "computer networks", "operating systems"
    );
    private final ResumeRepository resumes;

    public MatchResult match(String email, Job job) {
        ResumeDocument resume = resumes.findFirstByUserEmailOrderByUploadedAtDesc(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload a resume before matching jobs"));
        String resumeText = resume.getExtractedText().toLowerCase(Locale.ROOT);
        String requiredText = (job.getRequiredSkills() + " " + job.getDescription()).toLowerCase(Locale.ROOT);
        List<String> required = SKILL_CATALOG.stream().filter(skill -> containsSkill(requiredText, skill)).toList();
        List<String> matched = required.stream().filter(skill -> containsSkill(resumeText, skill)).toList();
        List<String> missing = required.stream().filter(skill -> !containsSkill(resumeText, skill)).toList();
        int skillScore = required.isEmpty() ? 0 : Math.round((matched.size() * 100f) / required.size());
        List<String> evidence = matched.stream().map(skill -> evidenceFor(resume.getExtractedText(), skill)).filter(Objects::nonNull).toList();
        int textScore = keywordOverlap(resumeText, requiredText);
        int score = required.isEmpty() ? textScore : Math.round(skillScore * .8f + textScore * .2f);
        List<String> nextSteps = missing.stream().limit(5).map(skill -> "Add concrete evidence for " + skill + " if you have used it; otherwise consider learning it.").toList();
        String explanation = String.format("%d%% = 80%% required-skill coverage (%d/%d) + 20%% general keyword overlap (%d%%). This is evidence alignment, not hiring probability.", score, matched.size(), required.size(), textScore);
        return new MatchResult(job.getId(), job.getTitle(), score, explanation, matched, missing, evidence, nextSteps);
    }

    private boolean containsSkill(String text, String skill) { return Pattern.compile("(?<![a-z0-9])" + Pattern.quote(skill) + "(?![a-z0-9])", Pattern.CASE_INSENSITIVE).matcher(text).find(); }
    private String evidenceFor(String original, String skill) {
        String lower = original.toLowerCase(Locale.ROOT); int index = lower.indexOf(skill.toLowerCase(Locale.ROOT)); if (index < 0) return null;
        int start = Math.max(0, original.lastIndexOf('\n', index) + 1); int end = original.indexOf('\n', index); if (end < 0) end = Math.min(original.length(), start + 240);
        return original.substring(start, end).trim();
    }
    private int keywordOverlap(String resume, String job) {
        Set<String> words = Arrays.stream(job.split("[^a-z0-9+#.]+" )).filter(w -> w.length() > 3).collect(Collectors.toSet());
        if (words.isEmpty()) return 0; long overlap = words.stream().filter(resume::contains).count(); return Math.min(100, Math.round(overlap * 100f / words.size()));
    }
}
