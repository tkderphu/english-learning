package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.dto.request.ChapterCreateRequest;
import site.viosmash.english.dto.request.PageRequest;
import site.viosmash.english.entity.Chapter;
import site.viosmash.english.repository.ChapterRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final PageService pageService;

    @Transactional
    public int create(ChapterCreateRequest request) {
        Chapter chapter = new Chapter();
        chapter.setTitle(request.getTitle());
        chapter.setDescription(request.getDescription());
        chapter.setBookId(request.getBookId());
        this.chapterRepository.save(chapter);
        return chapter.getId();
    }

}
