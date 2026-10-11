package com.joblens.api;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;import java.util.Map;
@RestController @RequestMapping("/api/health") public class HealthController { @GetMapping Map<String,Object> health(){return Map.of("status","UP","service","joblens-api","timestamp",Instant.now());} }
