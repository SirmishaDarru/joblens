package com.joblens.job;

import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Getter @Setter @NoArgsConstructor
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title; @Column(nullable=false) private String company; private String location; private String experienceLevel; private Integer salaryMin; private Integer salaryMax; private String source; private String sourceUrl;
    @Lob @Column(nullable=false) private String description; @Lob private String requiredSkills; private Instant postedAt = Instant.now();
}
