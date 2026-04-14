package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.request.BookReadingProgressRequest;
import site.viosmash.english.dto.response.*;
import site.viosmash.english.entity.Book;
import site.viosmash.english.entity.Sentence;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.*;
import site.viosmash.english.util.Util;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private static final int MIN_SECONDS_TO_LOG_READING = 30;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorBookRepository authorBookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final ChapterRepository chapterRepository;
    private final BookProgressRepository bookProgressRepository;
    private final PageRepository pageRepository;
    private final SentenceRepository sentenceRepository;
    private final Util util;
    private final ProfileLearningActivityService profileLearningActivityService;

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

    public PageResponse<BookResponse> recommend(int page, int limit) {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findAllByKeyword(pageable, null, userId, 1, null));
    }

    public PageResponse<AuthorResponse> getAuthors(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(authorRepository.findAllActive(pageable));
    }

    public PageResponse<BookResponse> getBooksByAuthor(int authorId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findAllByAuthorId(pageable, authorId));
    }

    public PageResponse<BookResponse> getFavorites(int page, int limit) {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findFavorites(pageable, userId));
    }

    public boolean favorite(int bookId, boolean isFavorite) {
        Integer userId = util.getCurrentUser().getId();
        var opt = bookProgressRepository.findByUserIdAndBookId(userId, bookId);
        if (opt.isPresent()) {
            var bp = opt.get();
            bp.setIsFavorite(isFavorite ? 1 : 0);
            bookProgressRepository.save(bp);
            return bp.getIsFavorite() != null && bp.getIsFavorite() == 1;
        }

        var bp = new site.viosmash.english.entity.BookProgress();
        bp.setUserId(userId);
        bp.setBookId(bookId);
        bp.setProgressPercent(0.0);
        bp.setIsFavorite(isFavorite ? 1 : 0);
        bookProgressRepository.save(bp);
        return bp.getIsFavorite() != null && bp.getIsFavorite() == 1;
    }

    public BookResponse getDetail(int id) {
        BookResponse bookResponse = bookRepository.findOneById(id);
        bookResponse.setChapters(chapterRepository.findAllByBookId(id));
        return bookResponse;
    }

    public List<BookPageResponse> getPagesByBook(int bookId, int offset, int limit) {
        Pageable pageable = new site.viosmash.english.util.OffsetPageRequest(offset, limit);
        List<BookPageResponse> pages = pageRepository.findByBookId(pageable, bookId);
        if (pages.isEmpty()) {
            return Collections.emptyList();
        }

        for (BookPageResponse page : pages) {
            page.setSentence(sentenceRepository.findAllByPageId(page.getId()));
        }

        return pages;
    }

    /**
     * Cập nhật tiến độ đọc và (nếu đủ thời gian) ghi nhật ký hoạt động LESSON cho heatmap.
     */
    @org.springframework.transaction.annotation.Transactional
    public void recordReadingProgress(int userId, int bookId, BookReadingProgressRequest req) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Book not found"));

        int lastPage = req.getLastReadPageNumber();
        LocalDateTime lastRead = req.getLastRead();
        long total = pageRepository.countByBookId(bookId);
        double pct = 0.0;
        if (total > 0) {
            pct = Math.min(100.0, (lastPage + 1.0) / (double) total * 100.0);
        }

        var opt = bookProgressRepository.findByUserIdAndBookId(userId, bookId);
        site.viosmash.english.entity.BookProgress bp;
        if (opt.isPresent()) {
            bp = opt.get();
        } else {
            bp = new site.viosmash.english.entity.BookProgress();
            bp.setUserId(userId);
            bp.setBookId(bookId);
            bp.setIsFavorite(0);
        }
        bp.setLastReadPageNumber(lastPage);
        bp.setProgressPercent(pct);
        bp.setLastReadTime(lastRead);
        bookProgressRepository.save(bp);

        Integer dur = req.getDuration();
        if (dur != null && dur >= MIN_SECONDS_TO_LOG_READING) {
            try {
                profileLearningActivityService.logBookReadingSession(
                        userId, bookId, book.getTitle(), dur, pct, lastPage);
            } catch (Exception ex) {
                log.warn("Could not record learning activity for book reading {}", bookId, ex);
            }
        }
    }
}
