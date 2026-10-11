package com.joblens.assistant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/assistant") @RequiredArgsConstructor
public class CareerAssistantController {
    private final CareerAssistantService assistant;
    @PostMapping("/ask") AssistantResponse ask(Authentication auth, @Valid @RequestBody AssistantRequest request) { return assistant.answer(auth.getName(), request); }
}
