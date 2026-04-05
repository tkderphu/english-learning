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

@RestController
@RequestMapping("/api/lookup")
@RequiredArgsConstructor
@Tag(name = "AI Lookup", description = "AI lookup/word lookup endpoints")
public class AiLookupController {

    private final AiLookupService aiLookupService;

    @Operation(summary = "Lookup text/word with AI")
    @PostMapping("/v1")
    public ResponseEntity<?> lookup(@RequestBody TextLookupRequest request) {
        TextLookupResponse res = aiLookupService.lookupText(request.getText());
        return ResponseEntity.ok(BaseResponse.success(res));
    }
}
