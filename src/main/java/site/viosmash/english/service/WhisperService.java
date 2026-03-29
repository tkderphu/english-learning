package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import site.viosmash.english.dto.response.WhisperSentenceResponse;
import site.viosmash.english.exception.ServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class WhisperService {

    @Value("${whisper.server.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    /**
     * Call external whisper server to transcribe + translate an audio URL to Vietnamese.
     * Expects the remote service to accept a JSON body like {"audioUrl":"...","targetLang":"vi"}
     * and return a JSON array matching {@link WhisperSentenceResponse}.
     *
     * @param audioUrl publicly accessible URL of the audio to transcribe
     * @return list of sentence-level transcription/translation results
     */
    public List<WhisperSentenceResponse> transcribe(String audioUrl) {
        if (audioUrl == null || audioUrl.trim().isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "audioUrl is required");
        }

        String endpoint = baseUrl;
        if (!endpoint.endsWith("/")) {
            endpoint = endpoint + "/";
        }
        // default path on whisper server; can be changed by setting a full URL in whisper.server.url
        endpoint = endpoint + "transcribe";

        Map<String, Object> body = new HashMap<>();
        body.put("audioUrl", audioUrl);
        body.put("targetLang", "vi");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<List<WhisperSentenceResponse>> resp = restTemplate.exchange(
                    endpoint,
                    org.springframework.http.HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<WhisperSentenceResponse>>() {}
            );

            if (resp.getStatusCode().is2xxSuccessful()) {
                return resp.getBody();
            }

            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Whisper server returned " + resp.getStatusCodeValue());

        } catch (RestClientException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Whisper request failed: " + e.getMessage());
        }
    }
}
