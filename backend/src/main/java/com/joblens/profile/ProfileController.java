package com.joblens.profile;

import com.joblens.user.UserRepository;
import jakarta.validation.Valid; import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.Set;

@RestController @RequestMapping("/api/profile") @RequiredArgsConstructor
public class ProfileController {
    private final CandidateProfileRepository profiles; private final UserRepository users;
    record ProfileRequest(String headline, String location, String preferredLocations, String experienceLevel, @NotNull Set<String> skills, String education) {}
    @GetMapping CandidateProfile get(Authentication auth) { return profiles.findByUserEmail(auth.getName()).orElseGet(() -> create(auth)); }
    @PutMapping CandidateProfile update(Authentication auth, @Valid @RequestBody ProfileRequest req) { CandidateProfile p = profiles.findByUserEmail(auth.getName()).orElseGet(() -> create(auth)); p.setHeadline(req.headline()); p.setLocation(req.location()); p.setPreferredLocations(req.preferredLocations()); p.setExperienceLevel(req.experienceLevel()); p.setSkills(req.skills()); p.setEducation(req.education()); return profiles.save(p); }
    private CandidateProfile create(Authentication auth) { CandidateProfile p = new CandidateProfile(); p.setUser(users.findByEmail(auth.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))); return profiles.save(p); }
}
