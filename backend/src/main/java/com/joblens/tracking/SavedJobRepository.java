package com.joblens.tracking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByUserEmailOrderBySavedAtDesc(String email);
    Optional<SavedJob> findByUserEmailAndJobId(String email, Long jobId);
}
