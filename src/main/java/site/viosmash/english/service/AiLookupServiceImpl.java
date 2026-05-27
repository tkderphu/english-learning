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

/**
 * AiLookupServiceImpl – Triển khai dịch vụ tra cứu từ vựng bằng Groq AI.
 *
 * <p>Luồng xử lý khi nhận một đoạn text cần tra cứu:
 * <ol>
 *   <li>Build system prompt yêu cầu AI trả về <b>JSON thuần tuý</b>
 *       (không markdown, không giải thích thêm).</li>
 *   <li>Xác định model sẽ dùng: ưu tiên {@code openai.model.lookup},
 *       fallback sang {@code openai.model.roleplay} nếu chưa cấu hình.</li>
 *   <li>Gọi {@link GroqAiClient#createTextResponse(OpenAiTextRequest)}
 *       qua Spring WebClient.</li>
 *   <li>Parse chuỗi JSON trả về thành {@link TextLookupResponse}.</li>
 *   <li>Nếu AI trả về JSON bọc trong markdown code block, dùng
 *       {@link #extractJson(String)} để bóc tách trước khi parse.</li>
 *   <li>Tự động điền {@code audioUrl} bằng Google TTS URL.</li>
 * </ol>
 */
@Service
@RequiredArgsConstructor
public class AiLookupServiceImpl implements AiLookupService {

    private final GroqAiClient groqAiClient;
    private final ObjectMapper objectMapper;

    /** Model AI dùng riêng cho tra cứu từ (ưu tiên). */
    @Value("${openai.model.lookup:}")
    private String lookupModel;

    /** Model AI dùng cho roleplay, dùng làm fallback khi lookupModel chưa cấu hình. */
    @Value("${openai.model.roleplay:}")
    private String roleplayModel;

    /**
     * Tra cứu nghĩa, phiên âm và ví dụ của một từ/cụm từ tiếng Anh bằng Groq AI.
     *
     * <p>Build prompt yêu cầu AI trả về đúng định dạng JSON với 5 trường:
     * {@code selectedText}, {@code meaning} (tiếng Việt), {@code phonetic},
     * {@code audioUrl}, {@code examples}. Sau khi nhận response, tự động
     * gắn {@code audioUrl} bằng Google TTS để Android client phát âm trực tiếp.</p>
     *
     * <p>Có hai lớp xử lý response:
     * <ol>
     *   <li><b>Primary</b>: {@code objectMapper.readValue(aiRaw, TextLookupResponse.class)}
     *       – parse trực tiếp nếu AI trả về JSON thuần.</li>
     *   <li><b>Fallback</b>: {@link #extractJson(String)} để tách JSON ra khỏi
     *       markdown block (```json ... ```) trước khi parse lại.</li>
     * </ol>
     *
     * @param text Từ hoặc cụm từ tiếng Anh cần tra cứu (không được rỗng)
     * @return {@link TextLookupResponse} chứa nghĩa, phiên âm, ví dụ và URL phát âm
     * @throws ServiceException {@code 400} nếu text rỗng;
     *                          {@code 502 Bad Gateway} nếu AI gặp lỗi hoặc trả về không hợp lệ
     */
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
            // Parse trực tiếp nếu AI trả về JSON thuần
            TextLookupResponse res = objectMapper.readValue(aiRaw, TextLookupResponse.class);
            res.setAudioUrl(String.format(
                    "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=en&client=tw-ob",
                    res.getSelectedText()
            ));
            return res;

        } catch (Exception ex) {
            // Fallback: bóc tách JSON ra khỏi markdown code block rồi parse lại
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

    /**
     * Tách phần JSON ra khỏi chuỗi text (thường là markdown code block).
     *
     * <p>Tìm ký tự '{' đầu tiên và '}' cuối cùng trong chuỗi để
     * cắt lấy đúng phần JSON object. Dùng khi AI trả về dạng:
     * <pre>
     *   ```json
     *   { "meaning": "..." }
     *   ```
     * </pre>
     *
     * @param text Chuỗi thô từ AI response
     * @return Chuỗi JSON thuần hoặc {@code null} nếu không tìm thấy
     */
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
