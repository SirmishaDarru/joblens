package com.joblens.tracking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserEmailOrderByUpdatedAtDesc(String email);
    Optional<Application> findByUserEmailAndId(String email, Long id);
    Optional<Application> findByUserEmailAndJobId(String email, Long jobId);
}
