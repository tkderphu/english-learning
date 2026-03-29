package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.PageRequest;
import site.viosmash.english.dto.request.SentenceCreateRequest;
import site.viosmash.english.entity.Page;
import site.viosmash.english.repository.PageRepository;

@RequiredArgsConstructor
@Service
public class PageService {

    private final PageRepository pageRepository;

    private final SentenceService sentenceService;

    public void create(PageRequest request) {
        for(String content : request.getContent()) {
            Page page = new Page();
            page.setChapterId(request.getChapterId());
            page.setAudioId(null);
            page.setContent(content);
            pageRepository.save(page);

            SentenceCreateRequest sentenceCreateRequest = new SentenceCreateRequest();
            sentenceCreateRequest.setPageId(page.getId());
            sentenceService.create(sentenceCreateRequest);
        }
    }
}
