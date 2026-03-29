package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.SentenceCreateRequest;
import site.viosmash.english.dto.response.WhisperSentenceResponse;
import site.viosmash.english.entity.Audio;
import site.viosmash.english.entity.Page;
import site.viosmash.english.entity.Sentence;
import site.viosmash.english.repository.AudioRepository;
import site.viosmash.english.repository.PageRepository;
import site.viosmash.english.repository.SentenceRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    private final PageRepository pageRepository;

    private final WhisperService whisperService;

    private final AudioRepository audioRepository;

    public void create(SentenceCreateRequest request) {
        Page page = pageRepository.findById(request.getPageId())
                .orElseThrow(() -> new IllegalArgumentException("not found page with id: " + request.getPageId()));

        Audio audio = audioRepository.findById(page.getAudioId())
                .orElseThrow(() -> new IllegalArgumentException("not found page with id: " + page.getAudioId()));

        List<WhisperSentenceResponse> whisperSentences = whisperService.transcribe(audio.getFileUrl());

        List<Sentence> sentences = whisperSentences.stream().map(sentence -> {
            Sentence s = new Sentence();
            s.setContent(sentence.getEnglishText());
            s.setTranscription1(sentence.getVietnameseText());
            s.setStartTime(sentence.getStartTimeMs());
            s.setEndTime(sentence.getEndTimeMs());
            s.setPageId(request.getPageId());

            return s;
        }).toList();

        sentenceRepository.saveAll(sentences);
    }
}
