package com.joblens.matching;

import com.joblens.job.Job;
import com.joblens.job.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service @RequiredArgsConstructor
public class RecommendationService {
    private final JobRepository jobs;
    private final MatchingService matchingService;

    public List<JobRecommendation> recommend(String email, int limit) {
        int candidateLimit = Math.min(Math.max(limit, 1), 100);
        List<Job> candidates = jobs.findAll(PageRequest.of(0, candidateLimit * 3)).getContent();
        return candidates.stream()
                .map(job -> new JobRecommendation(job, matchingService.match(email, job)))
                .sorted(Comparator.comparingInt((JobRecommendation recommendation) -> recommendation.match().score()).reversed())
                .limit(candidateLimit)
                .toList();
    }
}
