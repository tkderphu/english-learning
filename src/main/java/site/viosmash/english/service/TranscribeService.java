package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.viosmash.english.dto.request.SubtitleGenerationRequest;

@Service
@RequiredArgsConstructor
public class TranscribeService {

    @Value("${whisper.service.url}")
    private String whisperServiceUrl;

    private final RestTemplate restTemplate;

    public void transcribeAudio(String audioUrl) {
        SubtitleGenerationRequest request = SubtitleGenerationRequest.builder()
                .audioUrl(audioUrl)
                .language("en")
                .build();

        Object object = restTemplate.postForObject(whisperServiceUrl, null, null);
    }
}
