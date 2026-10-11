package com.joblens.resume;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ProfileExtractionService {
    private static final List<String> SKILLS = List.of("Java", "Python", "C++", "JavaScript", "TypeScript", "SQL", "MySQL", "PostgreSQL", "MongoDB", "Spring Boot", "React", "Node.js", "REST APIs", "Docker", "Kubernetes", "AWS", "Git", "Linux", "System Design", "Data Structures", "Algorithms", "Machine Learning", "Natural Language Processing", "TensorFlow", "PyTorch", "Multithreading");

    public ExtractedProfile extract(String text) {
        String normalized = text.replace("\r", "");
        List<String> skills = SKILLS.stream().filter(skill -> contains(normalized, skill)).toList();
        List<String> education = section(normalized, "education", "academic", "qualifications");
        List<String> experience = section(normalized, "experience", "employment", "work history", "internship");
        List<String> projects = section(normalized, "projects", "personal projects", "academic projects");
        List<String> warnings = new ArrayList<>();
        if (skills.isEmpty()) warnings.add("No catalogued skills were detected; review the extracted text.");
        if (experience.isEmpty()) warnings.add("No experience section was confidently detected.");
        if (projects.isEmpty()) warnings.add("No projects section was confidently detected.");
        return new ExtractedProfile(skills, education, experience, projects, warnings);
    }

    private boolean contains(String text, String phrase) { return Pattern.compile("(?<![A-Za-z0-9])" + Pattern.quote(phrase) + "(?![A-Za-z0-9])", Pattern.CASE_INSENSITIVE).matcher(text).find(); }

    private List<String> section(String text, String... headings) {
        String lower = text.toLowerCase(Locale.ROOT);
        int start = -1; for (String heading : headings) { int candidate = lower.indexOf(heading); if (candidate >= 0 && (start < 0 || candidate < start)) start = candidate; }
        if (start < 0) return List.of();
        int contentStart = text.indexOf('\n', start); if (contentStart < 0) contentStart = start;
        int end = text.length(); String[] stops = {"\neducation", "\nexperience", "\nprojects", "\nskills", "\nachievements", "\ncertifications"};
        for (String stop : stops) { int candidate = lower.indexOf(stop, contentStart + 1); if (candidate >= 0 && candidate < end) end = candidate; }
        return Arrays.stream(text.substring(contentStart, end).split("\\n" )).map(String::trim).filter(line -> line.length() > 2).limit(30).toList();
    }
}
