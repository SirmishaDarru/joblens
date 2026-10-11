package com.joblens.profile;

import com.joblens.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor
public class CandidateProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false) @JoinColumn(name = "user_id", unique = true) private User user;
    private String headline; private String location; private String preferredLocations; private String experienceLevel;
    @ElementCollection private Set<String> skills = new HashSet<>();
    @Lob private String education;
}
