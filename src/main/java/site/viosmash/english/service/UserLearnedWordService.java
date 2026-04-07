package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class UserLearnedWordService {

    private final UserLearnedWordRepository repository;

    public Page<UserLearnedWordResponse> list(Integer userId, String filter, String q, Pageable pageable) {
        String f = filter == null ? "ALL" : filter.trim().toUpperCase();
        boolean hasQ = q != null && !q.isBlank();
        String needle = hasQ ? q.trim() : "";

        Page<UserLearnedWord> page;
        if (hasQ) {
            page = switch (f) {
                case "FAVORITES", "FAVORITE" ->
                        repository.findByUserIdAndFavoriteTrueAndTermContainingIgnoreCaseOrderByCreatedAtDesc(userId, needle, pageable);
                case "NEEDS_ATTENTION", "DIFFICULT" ->
                        repository.findByUserIdAndNeedsAttentionTrueAndTermContainingIgnoreCaseOrderByCreatedAtDesc(userId, needle, pageable);
                default -> repository.findByUserIdAndTermContainingIgnoreCaseOrderByCreatedAtDesc(userId, needle, pageable);
            };
        } else {
            page = switch (f) {
                case "FAVORITES", "FAVORITE" -> repository.findByUserIdAndFavoriteTrueOrderByCreatedAtDesc(userId, pageable);
                case "NEEDS_ATTENTION", "DIFFICULT" -> repository.findByUserIdAndNeedsAttentionTrueOrderByCreatedAtDesc(userId, pageable);
                default -> repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
            };
        }
        return page.map(UserLearnedWordService::toResponse);
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
