package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.viosmash.english.dto.request.TextLookupRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.TextLookupResponse;
import site.viosmash.english.service.AiLookupService;

/**
 * AiLookupController – Endpoint tra cứu từ vựng / cụm từ bằng AI.
 *
 * <p>Tiếp nhận từ hoặc cụm từ tiếng Anh mà người dùng bôi đen trong khi
 * đọc sách, sau đó uỷ quyền cho {@link AiLookupService#lookupText(String)}
 * để gọi Groq AI (Llama model) và trả về kết quả tra cứu dạng JSON.</p>
 *
 * <p>Base path: {@code /api/lookup}</p>
 */
@RestController
@RequestMapping("/api/lookup")
@RequiredArgsConstructor
@Tag(name = "AI Lookup", description = "AI lookup/word lookup endpoints")
public class AiLookupController {

    private final AiLookupService aiLookupService;

    /**
     * Tra cứu từ/cụm từ bằng AI – {@code POST /api/lookup/v1}
     *
     * <p>Nhận đầu vào là một đoạn text (từ đơn hoặc cụm từ) từ nội dung sách,
     * gọi {@code aiLookupService.lookupText()} để xử lý bằng Groq AI và
     * trả về đối tượng {@link TextLookupResponse} bao gồm:
     * <ul>
     *   <li>{@code meaning}: nghĩa tiếng Việt, rõ ràng và dễ hiểu</li>
     *   <li>{@code phonetic}: phiên âm IPA (nullable)</li>
     *   <li>{@code audioUrl}: URL phát âm qua Google TTS (tự động điền)</li>
     *   <li>{@code examples}: danh sách câu ví dụ tiếng Anh (nullable)</li>
     * </ul>
     *
     * @param request Body chứa trường {@code text} – từ/cụm từ cần tra cứu
     * @return {@code 200 OK} với {@link TextLookupResponse} | {@code 400} nếu text rỗng
     *         | {@code 502 Bad Gateway} nếu AI provider gặp lỗi
     */
    @Operation(summary = "Lookup text/word with AI")
    @PostMapping("/v1")
    public ResponseEntity<?> lookup(@RequestBody TextLookupRequest request) {
        TextLookupResponse res = aiLookupService.lookupText(request.getText());
        return ResponseEntity.ok(BaseResponse.success(res));
    }
}
