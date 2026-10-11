package com.joblens.matching;

import java.util.List;

public record MatchResult(
        Long jobId,
        String jobTitle,
        int score,
        String scoreExplanation,
        List<String> matchedSkills,
        List<String> missingSkills,
        List<String> evidence,
        List<String> nextSteps
) {}
