package com.joblens.tracking;

import com.joblens.job.Job;
import com.joblens.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="saved_jobs", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","job_id"}))
@Getter @Setter @NoArgsConstructor
public class SavedJob {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private User user;
    @ManyToOne(optional=false) private Job job;
    @Column(nullable=false) private Instant savedAt = Instant.now();
}
