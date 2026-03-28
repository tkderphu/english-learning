package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.request.CreateChatSessionRequest;
import site.viosmash.english.dto.request.SendTextMessageRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.AiChatService;
import site.viosmash.english.dto.response.DeleteChatSessionResponse;

@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
@Tag(name = "AI Chat", description = "AI roleplay chat endpoints")
public class AiChatController {

    private final AiChatService aiChatService;

    @Operation(summary = "Create a new AI chat session")
    @PostMapping("/v1/sessions")
    public ResponseEntity<?> createSession(@RequestBody CreateChatSessionRequest request) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.createSession(request)));
    }

    @Operation(summary = "Send a text message to AI")
    @PostMapping("/v1/sessions/{sessionId}/messages/text")
    public ResponseEntity<?> sendTextMessage(
            @PathVariable Integer sessionId,
            @RequestBody SendTextMessageRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.sendTextMessage(sessionId, request)));
    }

    @Operation(summary = "Send a voice message to AI")
    @PostMapping(
            value = "/v1/sessions/{sessionId}/messages/voice",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> sendVoiceMessage(
            @PathVariable Integer sessionId,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "audioDuration", required = false) Integer audioDuration
    ) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.sendVoiceMessage(sessionId, audio, audioDuration)));
    }

    @Operation(summary = "End an AI chat session")
    @PostMapping("/v1/sessions/{sessionId}/end")
    public ResponseEntity<?> endSession(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.endSession(sessionId)));
    }

    @Operation(summary = "Get AI chat session history by user")
    @GetMapping("/v1/sessions")
    public ResponseEntity<?> getSessionHistory() {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.getSessionHistory()));
    }

    @Operation(summary = "Get messages of one session")
    @GetMapping("/v1/sessions/{sessionId}/messages")
    public ResponseEntity<?> getSessionMessages(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.getSessionMessages(sessionId)));
    }

    @Operation(summary = "Get session detail (role/instruction + summary)")
    @GetMapping("/v1/sessions/{sessionId}/detail")
    public ResponseEntity<?> getSessionDetail(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.getSessionDetail(sessionId)));
    }

    @Operation(summary = "Get read-only transcript with user feedback")
    @GetMapping("/v1/sessions/{sessionId}/transcript")
    public ResponseEntity<?> getSessionTranscript(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(BaseResponse.success(aiChatService.getSessionTranscript(sessionId)));
    }

    @Operation(summary = "Delete a completed chat session (owner only)")
    @DeleteMapping("/v1/sessions/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable Integer sessionId) {
        DeleteChatSessionResponse res = aiChatService.deleteSession(sessionId);
        return ResponseEntity.ok(BaseResponse.success(res));
    }
}
