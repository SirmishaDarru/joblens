package com.joblens.assistant;

import jakarta.validation.constraints.NotBlank;

public record AssistantRequest(@NotBlank String question, Long jobId) {}
