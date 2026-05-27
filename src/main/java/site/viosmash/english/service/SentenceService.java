package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.response.SentenceResponse;
import site.viosmash.english.repository.SentenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    public List<SentenceResponse> getListByPageId(int pageId) {
        return sentenceRepository.findAllByPageId(pageId);
    }
}
