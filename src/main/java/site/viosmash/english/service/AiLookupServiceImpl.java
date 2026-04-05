package site.viosmash.english.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.response.TextLookupResponse;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.groqapi.GroqAiClient;
import site.viosmash.english.dto.request.OpenAiTextRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiLookupServiceImpl implements AiLookupService {

    private final GroqAiClient groqAiClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model.lookup:}")
    private String lookupModel;

    @Value("${openai.model.roleplay:}")
    private String roleplayModel;

    @Override
    public TextLookupResponse lookupText(String text) {
        if (text == null || text.isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "text is required");
        }

        // Build a simple prompt asking the AI to return strict JSON matching our response shape.
        String prompt = String.format(
                "Return a JSON object with fields: selectedText, meaning, phonetic (nullable), audioUrl (nullable), examples (array of strings, nullable).\nSelected text: %s\n", text.trim());

    String modelToUse = (lookupModel == null || lookupModel.isBlank()) ? roleplayModel : lookupModel;

    OpenAiTextRequest request = OpenAiTextRequest.builder()
        .model(modelToUse)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(Map.of("type", "input_text", "text", "You are an English dictionary/lookup service. Produce only JSON in the exact shape requested."))
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(Map.of("type", "input_text", "text", prompt))
                        )
                ))
                .build();

        String aiRaw;
        try {
            aiRaw = groqAiClient.createTextResponse(request);
        } catch (RuntimeException ex) {
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "AI provider error: " + ex.getMessage());
        }

        try {
            // Try to parse JSON from the AI response. Allow the AI to return either the object or a JSON string inside text.
            TextLookupResponse res = objectMapper.readValue(aiRaw, TextLookupResponse.class);
            res.setAudioUrl(String.format("https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=en&client=tw-ob", res.getSelectedText()));
            return res;
        } catch (Exception ex) {
            // As a fallback, attempt to extract JSON substring from the AI output.
            try {
                String json = extractJson(aiRaw);
                if (json == null) {
                    throw new ServiceException(HttpStatus.BAD_GATEWAY, "AI response not JSON: " + aiRaw);
                }
                TextLookupResponse textLookupResponse = objectMapper.readValue(json, TextLookupResponse.class);
                textLookupResponse.setAudioUrl(String.format("https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=en&client=tw-ob", textLookupResponse.getSelectedText()));

                return textLookupResponse;
            } catch (ServiceException se) {
                throw se;
            } catch (Exception ex2) {
                throw new ServiceException(HttpStatus.BAD_GATEWAY, "Failed to parse AI response: " + ex2.getMessage());
            }
        }
    }

    private String extractJson(String text) {
        if (text == null) return null;
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return null;
    }
}
