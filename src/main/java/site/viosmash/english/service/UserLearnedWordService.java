package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.dto.request.UserLearnedWordCreateRequest;
import site.viosmash.english.dto.request.UserLearnedWordPatchRequest;
import site.viosmash.english.dto.response.UserLearnedWordResponse;
import site.viosmash.english.entity.UserLearnedWord;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.UserLearnedWordRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLearnedWordService {

    private final UserLearnedWordRepository repository;
    private final NamedParameterJdbcTemplate jdbc;

    public Page<UserLearnedWordResponse> list(Integer userId, String filter, String q, Pageable pageable) {
        String search = (q == null || q.isBlank()) ? null : q.trim().toLowerCase();
        var params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("search", search)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        String whereSearch = search == null ? "" : " AND LOWER(TRIM(f.term)) LIKE CONCAT('%', :search, '%') ";

        String countSql = """
                SELECT COUNT(*) FROM (
                    SELECT LOWER(TRIM(f.term)) AS norm_term
                    FROM flashcards f
                    JOIN decks d ON d.id = f.deck_id
                    WHERE d.user_id = :userId
                      AND f.term IS NOT NULL
                      AND TRIM(f.term) <> ''
                """ + whereSearch + """
                    GROUP BY LOWER(TRIM(f.term))
                ) t
                """;
        Long total = jdbc.queryForObject(countSql, params, Long.class);

        String dataSql = """
                SELECT
                    -CAST(MOD(CRC32(LOWER(TRIM(MAX(f.term)))), 2000000000) AS SIGNED) AS id,
                    MAX(TRIM(f.term)) AS term,
                    NULL AS phonetic,
                    MAX(NULLIF(TRIM(f.definition), '')) AS definition,
                    NULL AS source_module,
                    0 AS favorite,
                    0 AS needs_attention,
                    NULL AS audio_url,
                    MAX(f.created_at) AS created_at
                FROM flashcards f
                JOIN decks d ON d.id = f.deck_id
                WHERE d.user_id = :userId
                  AND f.term IS NOT NULL
                  AND TRIM(f.term) <> ''
                """ + whereSearch + """
                GROUP BY LOWER(TRIM(f.term))
                ORDER BY MAX(f.created_at) DESC, MAX(TRIM(f.term)) ASC
                LIMIT :limit OFFSET :offset
                """;

        List<UserLearnedWordResponse> content = jdbc.query(dataSql, params, (rs, i) ->
                UserLearnedWordResponse.builder()
                        .id(rs.getInt("id"))
                        .term(rs.getString("term"))
                        .phonetic(rs.getString("phonetic"))
                        .definition(rs.getString("definition"))
                        .sourceModule(rs.getString("source_module"))
                        .favorite(rs.getBoolean("favorite"))
                        .needsAttention(rs.getBoolean("needs_attention"))
                        .audioUrl(rs.getString("audio_url"))
                        .createdAt(rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime())
                        .build()
        );
        return new org.springframework.data.domain.PageImpl<>(content, pageable, total == null ? 0L : total);
    }

    @Transactional
    public UserLearnedWordResponse add(Integer userId, UserLearnedWordCreateRequest req) {
        String term = req.getTerm().trim();
        repository.findByUserIdAndTerm(userId, term).ifPresent(w -> {
            throw new ServiceException(HttpStatus.CONFLICT, "Word already exists: " + term);
        });

        UserLearnedWord w = new UserLearnedWord();
        w.setUserId(userId);
        w.setTerm(term);
        w.setPhonetic(req.getPhonetic());
        w.setDefinition(req.getDefinition());
        w.setSourceModule(req.getSourceModule());
        w.setFavorite(Boolean.TRUE.equals(req.getFavorite()));
        w.setNeedsAttention(Boolean.TRUE.equals(req.getNeedsAttention()));
        w.setAudioUrl(req.getAudioUrl());
        w.setCreatedAt(LocalDateTime.now());

        return toResponse(repository.save(w));
    }

    @Transactional
    public UserLearnedWordResponse patch(Integer userId, Integer wordId, UserLearnedWordPatchRequest req) {
        if (wordId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "wordId is required");
        }
        UserLearnedWord w = repository.findById(wordId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Word not found"));
        if (!w.getUserId().equals(userId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Access denied");
        }
        if (req.getFavorite() != null) {
            w.setFavorite(req.getFavorite());
        }
        if (req.getNeedsAttention() != null) {
            w.setNeedsAttention(req.getNeedsAttention());
        }
        return toResponse(repository.save(w));
    }

    private static UserLearnedWordResponse toResponse(UserLearnedWord w) {
        return UserLearnedWordResponse.builder()
                .id(w.getId())
                .term(w.getTerm())
                .phonetic(w.getPhonetic())
                .definition(w.getDefinition())
                .sourceModule(w.getSourceModule())
                .favorite(w.getFavorite())
                .needsAttention(w.getNeedsAttention())
                .audioUrl(w.getAudioUrl())
                .createdAt(w.getCreatedAt())
                .build();
    }
}
