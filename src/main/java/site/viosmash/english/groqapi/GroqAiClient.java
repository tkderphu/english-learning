package site.viosmash.english.groqapi;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.request.OpenAiTextRequest;
import site.viosmash.english.dto.response.OpenAiTranscriptionResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GroqAiClient {

    private final WebClient openAiWebClient;

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${ai.provider:openai}")
    private String provider;

    @Value("${ai.text.path:}")
    private String textPath;

    @Value("${openai.retry.max-attempts:3}")
    private int maxRetryAttempts;

    @Value("${openai.retry.initial-delay-ms:800}")
    private long initialRetryDelayMs;

    private void ensureApiKeyPresent() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("AI provider API key is missing. Set GROQ_API_KEY or config openai.api.key.");
        }
    }

    public String createTextResponse(OpenAiTextRequest request) {
        ensureApiKeyPresent();
        OpenAiTextRequest safeRequest = Objects.requireNonNull(request);
        Map<String, Object> requestBody = buildTextRequestBody(safeRequest);

        Map<String, Object> response = executeWithRetry(() -> openAiWebClient.post()
                .uri(Objects.requireNonNull(resolveTextUri()))
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .bodyValue(Objects.requireNonNull(requestBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block());

        String extracted = extractText(response);
        if (extracted == null || extracted.isBlank()) {
            throw new RuntimeException("AI provider returned empty text response");
        }

        return extracted;
    }

    public String transcribeAudio(MultipartFile audioFile, String model) {
        ensureApiKeyPresent();
        String safeModel = (model == null || model.isBlank()) ? "gpt-4o-mini-transcribe" : model;
        String fileName = audioFile.getOriginalFilename() == null ? "audio.wav" : audioFile.getOriginalFilename();
        String contentType = audioFile.getContentType() == null
                ? MediaType.APPLICATION_OCTET_STREAM_VALUE
                : audioFile.getContentType();

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("model", safeModel);
        builder.part("file", audioFile.getResource())
                .filename(Objects.requireNonNull(fileName))
                .contentType(Objects.requireNonNull(MediaType.parseMediaType(Objects.requireNonNull(contentType))));

        OpenAiTranscriptionResponse response = executeWithRetry(() -> openAiWebClient.post()
                .uri("/audio/transcriptions")
                .contentType(Objects.requireNonNull(MediaType.MULTIPART_FORM_DATA))
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(OpenAiTranscriptionResponse.class)
                .block());

        if (response == null || response.getText() == null || response.getText().isBlank()) {
            throw new RuntimeException("OpenAI returned empty transcription");
        }
        return response.getText().trim();
    }

    private <T> T executeWithRetry(CheckedSupplier<T> supplier) {
        int attempts = Math.max(1, maxRetryAttempts);
        long delayMs = Math.max(100L, initialRetryDelayMs);
        RuntimeException lastError = null;

        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                return supplier.get();
            } catch (WebClientResponseException ex) {
                lastError = ex;
                if (!shouldRetry(ex) || attempt == attempts) {
                    throw ex;
                }
            } catch (RuntimeException ex) {
                lastError = ex;
                if (attempt == attempts) {
                    throw ex;
                }
            }

            sleep(delayMs);
            delayMs = Math.min(delayMs * 2, 10_000L);
        }

        throw lastError != null ? lastError : new RuntimeException("OpenAI request failed");
    }

    private String resolveTextUri() {
        if (textPath != null && !textPath.isBlank()) {
            return textPath.trim();
        }
        return isGroqProvider() ? "/chat/completions" : "/responses";
    }

    private boolean isGroqProvider() {
        return provider != null && provider.trim().equalsIgnoreCase("groq");
    }

    private Map<String, Object> buildTextRequestBody(OpenAiTextRequest request) {
        if (isGroqProvider()) {
            Map<String, Object> body = new HashMap<>();
            body.put("model", request.getModel());
            body.put("messages", toChatMessages(request));
            return body;
        }
        // OpenAI Responses API body
        Map<String, Object> body = new HashMap<>();
        body.put("model", request.getModel());
        body.put("input", request.getInput());
        if (request.getText() != null) {
            body.put("text", request.getText());
        }
        return body;
    }

    private List<Map<String, Object>> toChatMessages(OpenAiTextRequest request) {
        if (request.getMessages() != null && !request.getMessages().isEmpty()) {
            return request.getMessages();
        }
        List<Map<String, Object>> output = new ArrayList<>();
        if (request.getInput() == null) {
            return output;
        }

        for (Map<String, Object> item : request.getInput()) {
            if (item == null) {
                continue;
            }
            String role = String.valueOf(item.getOrDefault("role", "user"));
            Object contentObj = item.get("content");
            String content = extractContentText(contentObj);

            Map<String, Object> message = new HashMap<>();
            message.put("role", role);
            message.put("content", content);
            output.add(message);
        }
        return output;
    }

    private String extractContentText(Object contentObj) {
        if (contentObj == null) {
            return "";
        }
        if (contentObj instanceof String s) {
            return s;
        }
        if (contentObj instanceof List<?> list) {
            StringBuilder sb = new StringBuilder();
            for (Object element : list) {
                if (element instanceof Map<?, ?> map) {
                    Object text = map.get("text");
                    if (text != null) {
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        sb.append(text);
                    }
                } else if (element != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(element);
                }
            }
            return sb.toString();
        }
        return String.valueOf(contentObj);
    }

    private String extractText(Map<String, Object> response) {
        if (response == null) {
            return null;
        }
        // OpenAI Responses API style: output[].content[].text
        Object outputObj = response.get("output");
        if (outputObj instanceof List<?> outputList) {
            for (Object out : outputList) {
                if (!(out instanceof Map<?, ?> outMap)) {
                    continue;
                }
                Object contentObj = outMap.get("content");
                if (!(contentObj instanceof List<?> contentList)) {
                    continue;
                }
                for (Object c : contentList) {
                    if (!(c instanceof Map<?, ?> cMap)) {
                        continue;
                    }
                    Object text = cMap.get("text");
                    if (text instanceof String textValue && !textValue.isBlank()) {
                        return textValue;
                    }
                }
            }
        }

        // Chat Completions style: choices[0].message.content
        Object choicesObj = response.get("choices");
        if (choicesObj instanceof List<?> choices && !choices.isEmpty()) {
            Object first = choices.get(0);
            if (first instanceof Map<?, ?> firstMap) {
                Object messageObj = firstMap.get("message");
                if (messageObj instanceof Map<?, ?> messageMap) {
                    Object contentObj = messageMap.get("content");
                    if (contentObj instanceof String content && !content.isBlank()) {
                        return content;
                    }
                    if (contentObj instanceof List<?> contentList) {
                        return extractContentText(contentList);
                    }
                }
                Object textObj = firstMap.get("text");
                if (textObj instanceof String text && !text.isBlank()) {
                    return text;
                }
            }
        }
        return null;
    }

    private boolean shouldRetry(WebClientResponseException ex) {
        int status = ex.getStatusCode().value();
        return status == 429 || status >= 500;
    }

    private void sleep(long delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", e);
        }
    }

    @FunctionalInterface
    private interface CheckedSupplier<T> {
        T get();
    }
}