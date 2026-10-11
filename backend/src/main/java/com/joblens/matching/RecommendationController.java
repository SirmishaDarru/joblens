package com.joblens.matching;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/recommendations") @RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    List<JobRecommendation> recommend(Authentication auth, @RequestParam(defaultValue = "10") int limit) {
        return recommendationService.recommend(auth.getName(), limit);
    }
}
