package com.joblens.tracking;

import com.joblens.job.JobRepository;
import com.joblens.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class TrackingController {
    private final SavedJobRepository savedJobs;
    private final ApplicationRepository applications;
    private final UserRepository users;
    private final JobRepository jobs;

    @GetMapping("/saved-jobs")
    List<SavedJob> savedJobs(Authentication auth) { return savedJobs.findByUserEmailOrderBySavedAtDesc(auth.getName()); }

    @PostMapping("/saved-jobs/{jobId}")
    SavedJob saveJob(Authentication auth, @PathVariable Long jobId) {
        return savedJobs.findByUserEmailAndJobId(auth.getName(), jobId).orElseGet(() -> {
            SavedJob saved = new SavedJob(); saved.setUser(currentUser(auth)); saved.setJob(jobs.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"))); return savedJobs.save(saved);
        });
    }

    @DeleteMapping("/saved-jobs/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unsaveJob(Authentication auth, @PathVariable Long jobId) { savedJobs.findByUserEmailAndJobId(auth.getName(), jobId).ifPresent(savedJobs::delete); }

    record ApplicationRequest(@NotNull ApplicationStatus status, String notes) {}

    @GetMapping("/applications")
    List<Application> applications(Authentication auth) { return applications.findByUserEmailOrderByUpdatedAtDesc(auth.getName()); }

    @PostMapping("/jobs/{jobId}/applications")
    Application createApplication(Authentication auth, @PathVariable Long jobId, @Valid @RequestBody ApplicationRequest request) {
        Application application = applications.findByUserEmailAndJobId(auth.getName(), jobId).orElseGet(() -> {
            Application a = new Application(); a.setUser(currentUser(auth)); a.setJob(jobs.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"))); return a;
        });
        application.setStatus(request.status()); application.setNotes(request.notes()); application.setUpdatedAt(Instant.now());
        if (application.getAppliedAt() == null && request.status() != ApplicationStatus.SAVED) application.setAppliedAt(Instant.now());
        return applications.save(application);
    }

    @PatchMapping("/applications/{applicationId}")
    Application updateApplication(Authentication auth, @PathVariable Long applicationId, @Valid @RequestBody ApplicationRequest request) {
        Application application = applications.findByUserEmailAndId(auth.getName(), applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        application.setStatus(request.status()); application.setNotes(request.notes()); application.setUpdatedAt(Instant.now());
        if (application.getAppliedAt() == null && request.status() != ApplicationStatus.SAVED) application.setAppliedAt(Instant.now());
        return applications.save(application);
    }

    private com.joblens.user.User currentUser(Authentication auth) { return users.findByEmail(auth.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)); }
}
