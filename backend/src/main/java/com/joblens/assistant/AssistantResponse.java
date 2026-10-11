package com.joblens.assistant;

import java.util.List;

public record AssistantResponse(String answer, List<String> citations, boolean grounded, String limitation) {}
