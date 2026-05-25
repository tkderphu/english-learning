package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.dto.request.ChapterCreateRequest;
import site.viosmash.english.entity.Chapter;
import site.viosmash.english.repository.ChapterRepository;

@RequiredArgsConstructor
@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final site.viosmash.english.util.Util util;

    @Transactional
    public int create(ChapterCreateRequest request) {
        Chapter chapter = new Chapter();
        chapter.setTitle(request.getTitle());
        chapter.setDescription(request.getDescription());
        chapter.setBookId(request.getBookId());
        chapter.setNumber(request.getNumber()); // Ensure number is set
        this.chapterRepository.save(chapter);
        return chapter.getId();
    }

    public site.viosmash.english.dto.response.PageResponse<site.viosmash.english.dto.response.ChapterResponse> getList(int bookId, int page, int limit) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page - 1, limit, org.springframework.data.domain.Sort.by("number").ascending());
        return util.convert(chapterRepository.findByBookIdPaginated(bookId, pageable));
    }

}
