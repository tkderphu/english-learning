package site.viosmash.english.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.OpenAiTextRequest;
import site.viosmash.english.dto.response.TextLookupResponse;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.groqapi.GroqAiClient;

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

        // Prompt yêu cầu nghĩa tiếng Việt + JSON strict
        String prompt = String.format(
                "Return ONLY a JSON object with fields:\n" +
                        "- selectedText (string)\n" +
                        "- meaning (Vietnamese, clear and easy to understand)\n" +
                        "- phonetic (nullable)\n" +
                        "- audioUrl (nullable)\n" +
                        "- examples (array of English sentences, nullable)\n" +
                        "Do not include any extra text or markdown.\n" +
                        "Selected text: %s\n",
                text.trim()
        );

        String modelToUse = (lookupModel == null || lookupModel.isBlank()) ? roleplayModel : lookupModel;

        OpenAiTextRequest request = OpenAiTextRequest.builder()
                .model(modelToUse)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(
                                        Map.of(
                                                "type", "input_text",
                                                "text", "You are an English dictionary/lookup service. Produce ONLY valid JSON. No explanation, no markdown."
                                        )
                                )
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(
                                        Map.of("type", "input_text", "text", prompt)
                                )
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
            // Parse trực tiếp
            TextLookupResponse res = objectMapper.readValue(aiRaw, TextLookupResponse.class);
            res.setAudioUrl(String.format(
                    "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=en&client=tw-ob",
                    res.getSelectedText()
            ));
            return res;

        } catch (Exception ex) {
            // Fallback: extract JSON
            try {
                String json = extractJson(aiRaw);
                if (json == null) {
                    throw new ServiceException(HttpStatus.BAD_GATEWAY, "AI response not JSON: " + aiRaw);
                }

                TextLookupResponse res = objectMapper.readValue(json, TextLookupResponse.class);
                res.setAudioUrl(String.format(
                        "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=en&client=tw-ob",
                        res.getSelectedText()
                ));

                return res;

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
