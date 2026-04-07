package site.viosmash.english.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.viosmash.english.dto.request.OpenAiTextRequest;
import site.viosmash.english.groqapi.GroqAiClient;


import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OpenAiTestController {

    private final GroqAiClient groqAiClient;

    @Value("${openai.model.roleplay}")
    private String model;

    @GetMapping("/api/test-openai")
    public String testOpenAi() {
        OpenAiTextRequest request = OpenAiTextRequest.builder()
                .model(model)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(
                                        Map.of("type", "input_text", "text", "You are a helpful English tutor.")
                                )
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(
                                        Map.of("type", "input_text", "text", "Say hello to a beginner English learner.")
                                )
                        )
                ))
                .build();

        return groqAiClient.createTextResponse(request);
    }
}