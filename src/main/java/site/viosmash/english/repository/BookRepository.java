package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.dto.response.BookResponse;
import site.viosmash.english.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query(value = """
        SELECT b.id, b.title, b.language, b.cover_url,
               COALESCE(GROUP_CONCAT(g.name SEPARATOR ', '), '') as genres,
               COALESCE(GROUP_CONCAT(a.name SEPARATOR ', '), '') as authors,
               b.status
        FROM book b
            LEFT JOIN author_book ab on ab.book_id = b.id
            LEFT JOIN author a on a.id = ab.author_id
            LEFT JOIN book_genre bg ON bg.book_id = b.id
            LEFT JOIN genre g ON g.id = bg.genre_id
            LEFT JOIN user_genre ug ON ug.genre_id = g.id
            LEFT JOIN user us ON us.id = ug.user_id
        WHERE (:keyword IS NULL OR LOWER(b.title) LIKE :keyword)
            AND (:isRecommend IS NULL OR us.id = :userId)
            AND (:genreId IS NULL OR g.id = :genreId)
        GROUP BY b.id
        ORDER BY b.id
        """,
        countQuery = """
        SELECT COUNT(b.id)
        FROM book b
            LEFT JOIN author_book ab on ab.book_id = b.id
            LEFT JOIN author a on a.id = ab.author_id
            LEFT JOIN book_genre bg ON bg.book_id = b.id
            LEFT JOIN genre g ON g.id = bg.genre_id
            LEFT JOIN user_genre ug ON ug.genre_id = g.id
            LEFT JOIN user us ON us.id = ug.user_id
        WHERE (:keyword IS NULL OR LOWER(b.title) LIKE :keyword)
            AND (:isRecommend IS NULL OR us.id = :userId)
            AND (:genreId IS NULL OR g.id = :genreId)
        GROUP BY b.id
        """, nativeQuery = true)
    Page<BookResponse> findAllByKeyword(Pageable pageable, @Param("keyword") String keyword,
                                        @Param("userId") Integer userId,
                                        @Param("isRecommend") Integer isRecommend,
                                        @Param("genreId") Integer genreId);


    @Query(value = """
        SELECT b.id, b.title, b.language, b.cover_url,
               COALESCE(
                  (
                      SELECT
                        GROUP_CONCAT(g.name SEPARATOR ', ')
                      FROM genre g
                        JOIN book_genre bg ON g.id = bg.genre_id
                      WHERE bg.book_id = b.id
                  ), '') as genres,
               COALESCE(
                  (
                      SELECT
                        GROUP_CONCAT(a.name SEPARATOR ', ')
                      FROM author_book ab
                         JOIN author a on a.id = ab.author_id
                      WHERE ab.book_id = b.id
                  ), '') as authors,
               bp.last_read_page_number, bp.progress_percent, bp.last_read_time,
               bp.is_favorite
        FROM book b
            JOIN book_progress bp ON bp.book_id = b.id
        WHERE bp.user_id = :userId
        """,
    nativeQuery = true,
    countQuery = """
        SELECT COUNT(b.id)
        FROM book b
            JOIN book_progress bp ON bp.book_id = b.id
        WHERE bp.user_id = :userId
    """)
    Page<BookResponse> findHistory(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = """
        SELECT b.id, b.title, b.language, b.cover_url,
               COALESCE(
                  (
                      SELECT
                        GROUP_CONCAT(g.name SEPARATOR ', ')
                      FROM genre g
                        JOIN book_genre bg ON g.id = bg.genre_id
                      WHERE bg.book_id = b.id
                  ), '') as genres,
               COALESCE(
                  (
                      SELECT
                        GROUP_CONCAT(a.name SEPARATOR ', ')
                      FROM author_book ab
                         JOIN author a on a.id = ab.author_id
                      WHERE ab.book_id = b.id
                  ), '') as authors,
               bp.last_read_page_number, bp.progress_percent, bp.last_read_time,
               bp.is_favorite
        FROM book b
            JOIN book_progress bp ON bp.book_id = b.id
        WHERE bp.user_id = :userId
          AND bp.is_favorite = 1
        ORDER BY bp.last_read_time DESC
        """,
    nativeQuery = true,
    countQuery = """
        SELECT COUNT(b.id)
        FROM book b
            JOIN book_progress bp ON bp.book_id = b.id
        WHERE bp.user_id = :userId
          AND bp.is_favorite = 1
    """)
    Page<BookResponse> findFavorites(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = """
        SELECT b.id, b.title, b.language, b.cover_url,
               COALESCE(GROUP_CONCAT(DISTINCT g.name SEPARATOR ', '), '') as genres,
               COALESCE(GROUP_CONCAT(DISTINCT a.name SEPARATOR ', '), '') as authors,
               b.status
        FROM book b
            JOIN author_book ab ON ab.book_id = b.id
            JOIN author a ON a.id = ab.author_id
            LEFT JOIN book_genre bg ON bg.book_id = b.id
            LEFT JOIN genre g ON g.id = bg.genre_id
        WHERE ab.author_id = :authorId
        GROUP BY b.id
        ORDER BY b.id
    """,
    countQuery = """
        SELECT COUNT(DISTINCT b.id)
        FROM book b
            JOIN author_book ab ON ab.book_id = b.id
        WHERE ab.author_id = :authorId
    """, nativeQuery = true)
    Page<BookResponse> findAllByAuthorId(Pageable pageable, @Param("authorId") Integer authorId);


    @Query(value = """
        SELECT b.id, b.title, b.language, b.cover_url,
               COALESCE(
                  (
                      SELECT
                        GROUP_CONCAT(g.name SEPARATOR ', ')
                      FROM genre g
                        JOIN book_genre bg ON g.id = bg.genre_id
                      WHERE bg.book_id = b.id
                  ), '') as genres,
               COALESCE(
                  (
                      SELECT
                        GROUP_CONCAT(a.name SEPARATOR ', ')
                      FROM author_book ab
                         JOIN author a on a.id = ab.author_id
                      WHERE ab.book_id = b.id
                  ), '') as authors,
               b.status
        FROM book b
        WHERE b.id = :id
    """, nativeQuery = true)
    BookResponse findOneById(@Param("id") int id);
}
