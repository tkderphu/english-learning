package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.request.BookReadingProgressRequest;
import site.viosmash.english.dto.response.*;
import site.viosmash.english.entity.Book;
import site.viosmash.english.entity.BookProgress;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.*;
import site.viosmash.english.util.Util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * BookService – Xử lý logic nghiệp vụ cho Sách.
 *
 * Bao gồm các logic phức tạp như tạo sách (liên kết tác giả, thể loại),
 * ghi nhận lịch sử đọc, và đề xuất sách dựa trên heatmap.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private static final int MIN_SECONDS_TO_LOG_READING = 30;
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter ISO_OFFSET_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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

    /** Query paginated books with optional keyword and genre filters. */
    public PageResponse<BookResponse> getList(int page, int limit, String keyword, Integer genreId) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findAllByKeyword(pageable, kw, null, null, genreId));
    }

    /**
     * Tạo sách mới kèm theo liên kết tác giả và thể loại.
     *
     * Lưu đối tượng Book vào CSDL. Sau đó, lặp qua danh sách authorIds
     * để lưu vào AuthorBookRepository (bảng author_book) và genreIds
     * để lưu vào BookGenreRepository (bảng book_genre).
     *
     * @param req DTO chứa thông tin metadata của sách
     * @return ID của sách vừa tạo
     */
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

    /** Return paginated reading history for current user. */
    public PageResponse<BookResponse> getHistory(int page, int limit) {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findHistory(pageable, userId));
    }

    /** Return personalized recommendations, fallback to general list if empty. */
    public PageResponse<BookResponse> recommend(int page, int limit) {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page - 1, limit);
        // Prefer personalized recommendations by user genres.
        var personalized = bookRepository.findAllByKeyword(pageable, null, userId, 1, null);
        if (personalized.hasContent()) {
            return util.convert(personalized);
        }
        // Fallback: return general book list so Home screen is never empty.
        return util.convert(bookRepository.findAllByKeyword(pageable, null, null, null, null));
    }

    /** Return paginated active authors. */
    public PageResponse<AuthorResponse> getAuthors(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(authorRepository.findAllActive(pageable));
    }

    /** Return paginated books of a given author. */
    public PageResponse<BookResponse> getBooksByAuthor(int authorId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findAllByAuthorId(pageable, authorId));
    }

    /** Return paginated favorites of current user. */
    public PageResponse<BookResponse> getFavorites(int page, int limit) {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findFavorites(pageable, userId));
    }

    @org.springframework.transaction.annotation.Transactional
    /** Update favorite state for a book of current user. */
    public boolean favorite(int bookId, boolean isFavorite) {
        Integer userId = util.getCurrentUser().getId();
        int favoriteValue = isFavorite ? 1 : 0;

        BookProgress bp = bookProgressRepository.findByUserIdAndBookId(userId, bookId)
                .orElseGet(() -> new BookProgress()
                        .setUserId(userId)
                        .setBookId(bookId)
                        .setProgressPercent(0.0));

        bp.setIsFavorite(favoriteValue);
        bookProgressRepository.save(bp);
        return favoriteValue == 1;
    }

    /** Return book detail with chapter list. */
    public BookResponse getDetail(int id) {
        Integer userId = null;
        if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
            userId = util.getCurrentUser().getId();
        }

        BookResponse bookResponse = bookRepository.findOneById(id, userId);
        bookResponse.setChapters(chapterRepository.findByBookIdPaginated(id, PageRequest.of(0, 100)).getContent());
        return bookResponse;
    }

    /** Return page slice and attach sentence data for read-book screen. */
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
     * Cập nhật tiến độ đọc và (nếu đủ thời gian) ghi nhật ký hoạt động BOOK cho heatmap.
     */
    @org.springframework.transaction.annotation.Transactional
    /** Persist reading progress and optionally log long reading sessions. */
    public void recordReadingProgress(int userId, int bookId, BookReadingProgressRequest req) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Book not found"));

        int lastPage = req.getLastReadPageNumber();
        LocalDateTime lastRead = parseLastRead(req.getLastRead());
        long total = pageRepository.countByBookId(bookId);
        double pct = 0.0;
        if (total > 0) {
            pct = Math.min(100.0, (lastPage + 1.0) / (double) total * 100.0);
        }

        BookProgress bp = bookProgressRepository.findByUserIdAndBookId(userId, bookId)
                .orElseGet(() -> new BookProgress()
                        .setUserId(userId)
                        .setBookId(bookId)
                        .setIsFavorite(0));

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

    /** Parse ISO datetime from client payload for last-read timestamp. */
    private LocalDateTime parseLastRead(String rawLastRead) {
        try {
            return LocalDateTime.parse(rawLastRead, ISO_LOCAL_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ignore) {
            try {
                return OffsetDateTime.parse(rawLastRead, ISO_OFFSET_DATE_TIME_FORMATTER)
                        .atZoneSameInstant(ZoneId.systemDefault())
                        .toLocalDateTime();
            } catch (DateTimeParseException ex) {
                throw new ServiceException(HttpStatus.BAD_REQUEST,
                        "Invalid lastRead format. Use ISO-8601 datetime, e.g. 2026-04-16T23:41:53.738916");
            }
        }
    }
}
