package com.joblens.resume;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfileExtractionServiceTest {
    @Test void extractsSkillsAndSections() {
        var profile = new ProfileExtractionService().extract("SKILLS\nJava, SQL, Spring Boot\n\nEXPERIENCE\nBackend Intern - built REST APIs\n\nPROJECTS\nInvestment Analytics Platform");
        assertTrue(profile.skills().contains("Java"));
        assertFalse(profile.experience().isEmpty());
        assertFalse(profile.projects().isEmpty());
    }
}
