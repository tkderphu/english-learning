package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.response.BookResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.entity.Book;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.*;
import site.viosmash.english.util.Util;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorBookRepository authorBookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final ChapterRepository chapterRepository;
    private final BookProgressRepository bookProgressRepository;
    private final Util util;

    public PageResponse<BookResponse> getList(int page, int limit, String keyword, Integer genreId) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        Pageable pageable = PageRequest.of(page - 1, limit);
        return util.convert(bookRepository.findAllByKeyword(pageable, kw, null, null, null, genreId));
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
        return util.convert(bookRepository.findAllByKeyword(pageable, null, userId, 1, null, null));
    }

    public List<BookResponse> recommend() {
        Integer userId = util.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(0, 10);

        List<BookResponse> recommended = bookRepository.findAllByKeyword(pageable, null, userId, null, 1, null).getContent();

        // Nếu không có gợi ý theo sở thích, lấy danh sách sách mới nhất làm mặc định
        if (recommended.isEmpty()) {
            recommended = bookRepository.findAllByKeyword(pageable, null, null, null, null, null).getContent();
        }
       return recommended;
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
}
