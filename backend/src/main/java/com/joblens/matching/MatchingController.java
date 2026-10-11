package com.joblens.matching;

import com.joblens.job.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/jobs") @RequiredArgsConstructor
public class MatchingController {
    private final JobRepository jobs;
    private final MatchingService matchingService;

    @GetMapping("/{jobId}/match")
    MatchResult match(@PathVariable Long jobId, Authentication auth) {
        return matchingService.match(auth.getName(), jobs.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found")));
    }
}
