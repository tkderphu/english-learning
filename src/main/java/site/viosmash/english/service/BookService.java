package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.response.*;
import site.viosmash.english.entity.Audio;
import site.viosmash.english.entity.Book;
import site.viosmash.english.entity.Sentence;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.*;
import site.viosmash.english.util.Util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorBookRepository authorBookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final ChapterRepository chapterRepository;
    private final BookProgressRepository bookProgressRepository;
    private final PageRepository pageRepository;
    private final SentenceRepository sentenceRepository;
    private final AudioRepository audioRepository;
    private final Util util;

    public PageResponse<BookResponse> getList(int page, int limit, String keyword, Integer genreId) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findAllByKeyword(pageable, kw, null, null, genreId));
    }

    public int create(BookCreateRequest req) {
        Book b = new Book();
        b.setTitle(req.getTitle());
        b.setLanguage(req.getLanguage());
        b.setCoverUrl(req.getCoverUrl());
        b.setStatus(1);
        try {
            b = bookRepository.save(b);
            int bookId = b.getId();
            if (req.getAuthorIds() != null) {
                req.getAuthorIds().forEach(aid -> {
                    var ab = new site.viosmash.english.entity.AuthorBook();
                    ab.setAuthorId(aid);
                    ab.setBookId(bookId);
                    authorBookRepository.save(ab);
                });
            }
            if (req.getGenreIds() != null) {
                req.getGenreIds().forEach(gid -> {
                    var bg = new site.viosmash.english.entity.BookGenre();
                    bg.setBookId(bookId);
                    bg.setGenreId(gid);
                    bookGenreRepository.save(bg);
                });
            }
        } catch (Exception ex) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create book");
        }
        return b.getId();
    }

    public PageResponse<BookResponse> getHistory(int page, int limit) {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findHistory(pageable, userId));
    }

    public List<BookResponse> recommend() {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(0, 10);
        return bookRepository.findAllByKeyword(pageable, null, userId, 1, null).getContent();
    }

    public void favorite(int bookId, boolean isFavorite) {
        Integer userId = util.getCurrentUser().getId();
        var opt = bookProgressRepository.findByUserIdAndBookId(userId, bookId);
        if (opt.isPresent()) {
            var bp = opt.get();
            bp.setIsFavorite(isFavorite ? 1 : 0);
            bookProgressRepository.save(bp);
            return;
        }

        var bp = new site.viosmash.english.entity.BookProgress();
        bp.setUserId(userId);
        bp.setBookId(bookId);
        bp.setProgressPercent(0.0);
        bp.setIsFavorite(isFavorite ? 1 : 0);
        bookProgressRepository.save(bp);
    }

    public BookResponse getDetail(int id) {
        BookResponse bookResponse = bookRepository.findOneById(id);
        bookResponse.setChapters(chapterRepository.findAllByBookId(id));
        return bookResponse;
    }

    public List<BookPageResponse> getPagesByBook(int bookId, List<Integer> pageNumbers) {
        List<site.viosmash.english.entity.Page> pages = pageRepository.findByBookIdAndNumbers(bookId, pageNumbers);
        if (pages.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> pageIds = pages.stream().map(site.viosmash.english.entity.Page::getId).toList();
        List<Integer> audioIds = pages.stream()
                .map(site.viosmash.english.entity.Page::getAudioId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Integer, Audio> audioMap = audioRepository.findAllById(audioIds).stream()
                .collect(Collectors.toMap(Audio::getId, Function.identity()));

        Map<Integer, List<Sentence>> sentenceMap = sentenceRepository.findByPageIdIn(pageIds).stream()
                .collect(Collectors.groupingBy(Sentence::getPageId));

        return pages.stream().map(page -> {
            AudioResponse audioResponse = null;
            if (page.getAudioId() != null) {
                Audio audio = audioMap.get(page.getAudioId());
                if (audio != null) {
                    audioResponse = new AudioResponse(
                            audio.getId(),
                            audio.getDuration(),
                            audio.getFormat(),
                            (int) audio.getSampleRate(),
                            (int) audio.getFileSize(),
                            audio.getFileUrl(),
                            page.getId()
                    );
                }
            }

            List<SentenceResponse> sentenceResponses = sentenceMap
                    .getOrDefault(page.getId(), Collections.emptyList())
                    .stream()
                    .map(s -> new SentenceResponse(
                            page.getId(),
                            s.getId(),
                            s.getContent(),
                            s.getTranscription1(),
                            (int) s.getStartTime(),
                            (int) s.getEndTime()
                    ))
                    .toList();

            return new BookPageResponse(page.getId(), page.getNumber(), audioResponse, sentenceResponses);
        }).toList();
    }
}
