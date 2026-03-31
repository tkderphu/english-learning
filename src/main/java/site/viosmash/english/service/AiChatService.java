package site.viosmash.english.service;

import site.viosmash.english.dto.request.CreateChatSessionRequest;
import site.viosmash.english.dto.request.SendTextMessageRequest;
import site.viosmash.english.dto.response.ChatMessageItemResponse;
import site.viosmash.english.dto.response.ChatMessageDetailItemResponse;
import site.viosmash.english.dto.response.ChatSessionHistoryResponse;
import site.viosmash.english.dto.response.CreateChatSessionResponse;
import site.viosmash.english.dto.response.DeleteChatSessionResponse;
import site.viosmash.english.dto.response.AiChatSessionDetailResponse;
import site.viosmash.english.dto.response.EndSessionResponse;
import site.viosmash.english.dto.response.SendTextMessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AiChatService {
    CreateChatSessionResponse createSession(CreateChatSessionRequest request);

    SendTextMessageResponse sendTextMessage(Integer sessionId, SendTextMessageRequest request);

    SendTextMessageResponse sendVoiceMessage(Integer sessionId, MultipartFile audioFile, Integer audioDuration);

    EndSessionResponse endSession(Integer sessionId);

    List<ChatSessionHistoryResponse> getSessionHistory();

    List<ChatMessageItemResponse> getSessionMessages(Integer sessionId);

    DeleteChatSessionResponse deleteSession(Integer sessionId);

    AiChatSessionDetailResponse getSessionDetail(Integer sessionId);

    List<ChatMessageDetailItemResponse> getSessionTranscript(Integer sessionId);
}
