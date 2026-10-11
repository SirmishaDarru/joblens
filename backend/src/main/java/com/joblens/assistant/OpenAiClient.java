package com.joblens.assistant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
public class OpenAiClient {
    private final RestClient client = RestClient.builder().baseUrl("https://api.openai.com/v1").build();
    private final ObjectMapper mapper; private final String apiKey; private final String model;
    public OpenAiClient(ObjectMapper mapper, @Value("${OPENAI_API_KEY:}") String apiKey, @Value("${OPENAI_MODEL:gpt-4o-mini}") String model){this.mapper=mapper;this.apiKey=apiKey;this.model=model;}
    public Optional<String> answer(String context, String question) {
        if(apiKey.isBlank()) return Optional.empty();
        try {
            Map<String,Object> body=Map.of("model",model,"temperature",0.1,"messages",List.of(Map.of("role","system","content","Answer only from the supplied context. Never invent candidate achievements. If unsupported, say so. Return concise plain text."),Map.of("role","user","content","CONTEXT:\n"+context+"\n\nQUESTION:\n"+question)));
            String raw=client.post().uri("/chat/completions").contentType(MediaType.APPLICATION_JSON).header("Authorization","Bearer "+apiKey).body(body).retrieve().body(String.class);
            JsonNode root=mapper.readTree(raw); JsonNode content=root.path("choices").path(0).path("message").path("content"); return content.isTextual()?Optional.of(content.asText()):Optional.empty();
        } catch(Exception ignored){return Optional.empty();}
    }
}
