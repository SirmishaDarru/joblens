package com.joblens.tracking;

import com.joblens.job.Job;
import com.joblens.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="applications", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","job_id"}))
@Getter @Setter @NoArgsConstructor
public class Application {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private User user;
    @ManyToOne(optional=false) private Job job;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private ApplicationStatus status = ApplicationStatus.SAVED;
    @Column(length=2000) private String notes;
    private Instant appliedAt;
    @Column(nullable=false) private Instant updatedAt = Instant.now();
}
