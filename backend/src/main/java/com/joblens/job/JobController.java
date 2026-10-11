package com.joblens.job;

import lombok.RequiredArgsConstructor; import org.springframework.data.domain.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/jobs") @RequiredArgsConstructor
public class JobController {
    private final JobRepository jobs;
    @GetMapping Page<Job> search(@RequestParam(defaultValue="") String q, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) { int safeSize=Math.min(Math.max(size,1),50); Pageable pageable=PageRequest.of(Math.max(page,0),safeSize,Sort.by(Sort.Direction.DESC,"postedAt")); return q.isBlank()?jobs.findAll(pageable):jobs.findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrLocationContainingIgnoreCase(q,q,q,pageable); }
    @GetMapping("/{id}") Job get(@PathVariable Long id) { return jobs.findById(id).orElseThrow(); }
}
