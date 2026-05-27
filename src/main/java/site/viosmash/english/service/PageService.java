package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.dto.request.PageRequest;
import site.viosmash.english.dto.request.SentenceCreateRequest;
import site.viosmash.english.dto.response.BookPageResponse;
import site.viosmash.english.dto.response.FileResponse;
import site.viosmash.english.dto.response.WhisperSentenceResponse;
import site.viosmash.english.entity.Audio;
import site.viosmash.english.entity.Page;
import site.viosmash.english.entity.Sentence;
import site.viosmash.english.repository.AudioRepository;
import site.viosmash.english.repository.PageRepository;
import site.viosmash.english.repository.SentenceRepository;
import site.viosmash.english.util.Util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

/**
 * PageService – Xử lý logic nghiệp vụ cho Trang sách.
 */
@RequiredArgsConstructor
@Service
public class PageService {

    private final PageRepository pageRepository;

    private final AudioRepository audioRepository;

    private final WhisperService whisperService;

    private final SentenceRepository sentenceRepository;

    public org.springframework.data.domain.Page<BookPageResponse> getListByChapterId(int chapterId,
                                                                                     int page,
                                                                                     int limit) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page - 1, limit, org.springframework.data.domain.Sort.by("number").ascending());

        org.springframework.data.domain.Page<BookPageResponse> result = pageRepository.findByChapterId(pageable, chapterId);
        return result;
    }

    /**
     * Tạo trang sách mới.
     *
     * Lưu entity Page, sau đó gọi whisperService.loadSamplePage() (hoặc transcribe)
     * để lấy danh sách câu tương ứng với audio, rồi dùng sentenceRepository.saveAll()
     * để lưu toàn bộ các câu (Sentence) của trang đó (phục vụ chức năng đọc theo câu).
     *
     * @param request Dữ liệu đầu vào gồm chapterId, audioId, number...
     * @return ID của trang sách vừa tạo
     * @throws IOException Nếu có lỗi thao tác với file
     * @throws UnsupportedAudioFileException Nếu file audio không được hỗ trợ
     */
    @Transactional
    public int create(PageRequest request) throws IOException, UnsupportedAudioFileException {
        Page page = new Page();
        page.setContent(request.getContent());
        page.setAudioId(request.getAudioId());
        page.setChapterId(request.getChapterId());
        page.setNumber(request.getNumber());

        this.pageRepository.save(page);

        Audio audio = this.audioRepository.findById(request.getAudioId()).orElse(null);
        if (audio == null) {
            throw new IllegalArgumentException("You must provide audio file");
        }
//        List<WhisperSentenceResponse> whisperSentences = whisperService.transcribe(audio.getFileUrl());

        List<WhisperSentenceResponse> whisperSentences = whisperService.loadSamplePage(page.getNumber());

        List<Sentence> sentences = whisperSentences.stream().map(sentence -> {
            Sentence s = new Sentence();
            s.setContent(sentence.getEnglishText());
            s.setTranscription1(sentence.getVietnameseText());
            s.setStartTime(sentence.getStartTimeMs());
            s.setEndTime(sentence.getEndTimeMs());
            s.setPageId(page.getId());

            return s;
        }).toList();

        this.sentenceRepository.saveAll(sentences);

        return page.getId();
    }
}
