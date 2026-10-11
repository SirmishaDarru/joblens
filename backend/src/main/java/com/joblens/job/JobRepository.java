package com.joblens.job;
import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface JobRepository extends JpaRepository<Job, Long> { Page<Job> findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String company, String location, Pageable pageable); }
