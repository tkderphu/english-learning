package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonalizedVocabularyService {

    private static final int LOOKBACK_MESSAGES_FOR_REPEAT = 4;
    private static final int MAX_PERSONAL_WORDS_PER_TURN = 4;

    private final NamedParameterJdbcTemplate jdbc;

    public List<String> selectWordsForTurn(
            Integer userId,
            AiChatSession session,
            List<AiChatMessage> recentMessages,
            String latestUserMessage,
            Integer nextTurn
    ) {
        if (userId == null || nextTurn == null || nextTurn <= 1 || !shouldInjectOnTurn(nextTurn)) {
            return List.of();
        }

        List<String> candidateTerms = loadFlashcardTerms(userId);
        if (candidateTerms.isEmpty()) {
            return List.of();
        }

        String latestNormalized = normalize(latestUserMessage);
        String sessionContext = normalize(
                safe(session.getTitle()) + " " + safe(session.getInstructionSnapshot()) + " " + safe(session.getAiRoleSnapshot())
        );
        String recentConversation = normalizeRecentConversation(recentMessages);

        List<String> filtered = candidateTerms.stream()
                .filter(w -> w != null && !w.isBlank())
                .filter(w -> !isRepeatedRecently(w, recentConversation))
                .sorted(Comparator
                        .comparingInt((String w) -> relevanceScore(w, latestNormalized, sessionContext))
                        .reversed())
                .toList();

        if (filtered.isEmpty()) {
            filtered = candidateTerms.stream()
                    .filter(w -> w != null && !w.isBlank())
                    .sorted(Comparator
                            .comparingInt((String w) -> relevanceScore(w, latestNormalized, sessionContext))
                            .reversed())
                    .toList();
        }

        Set<String> chosen = new LinkedHashSet<>();
        for (String w : filtered) {
            chosen.add(w.trim());
            if (chosen.size() >= MAX_PERSONAL_WORDS_PER_TURN) {
                break;
            }
        }

        return new ArrayList<>(chosen);
    }

    private boolean shouldInjectOnTurn(int turn) {
        return turn >= 2;
    }

    private int relevanceScore(String termValue, String latestMessage, String sessionContext) {
        int score = 0;
        String term = normalize(termValue);

        if (!term.isBlank() && containsToken(latestMessage, term)) score += 2;
        if (!term.isBlank() && containsToken(sessionContext, term)) score += 2;
        return score;
    }

    private boolean isRepeatedRecently(String term, String recentConversation) {
        return containsToken(recentConversation, normalize(term));
    }

    private String normalizeRecentConversation(List<AiChatMessage> recentMessages) {
        if (recentMessages == null || recentMessages.isEmpty()) return "";
        int from = Math.max(0, recentMessages.size() - LOOKBACK_MESSAGES_FOR_REPEAT);
        StringBuilder sb = new StringBuilder();
        for (AiChatMessage m : recentMessages.subList(from, recentMessages.size())) {
            sb.append(' ').append(safe(m.getContent()));
        }
        return normalize(sb.toString());
    }

    private boolean hasAnyOverlap(String left, String right) {
        if (left.isBlank() || right.isBlank()) return false;
        Set<String> leftTokens = tokenize(left);
        if (leftTokens.isEmpty()) return false;
        for (String token : tokenize(right)) {
            if (leftTokens.contains(token)) return true;
        }
        return false;
    }

    private List<String> loadFlashcardTerms(Integer userId) {
        Map<String, Object> params = Map.of("userId", userId);
        return jdbc.query(
                """
                SELECT term
                FROM (
                    SELECT LOWER(TRIM(f.term)) AS norm_term,
                           MAX(TRIM(f.term)) AS term,
                           MAX(f.created_at) AS latest_at
                    FROM flashcards f
                    JOIN decks d ON d.id = f.deck_id
                    WHERE d.user_id = :userId
                      AND d.status = 1
                      AND f.status = 1
                      AND f.term IS NOT NULL
                      AND TRIM(f.term) <> ''
                    GROUP BY LOWER(TRIM(f.term))
                ) t
                ORDER BY t.latest_at DESC
                LIMIT 20
                """,
                params,
                (rs, i) -> rs.getString("term")
        );
    }

    private Set<String> tokenize(String text) {
        if (text == null || text.isBlank()) return Set.of();
        String[] parts = normalize(text).split("\\s+");
        Set<String> tokens = new LinkedHashSet<>();
        for (String p : parts) {
            if (p.length() >= 3) tokens.add(p);
        }
        return tokens;
    }

    private boolean containsToken(String haystack, String token) {
        if (haystack == null || haystack.isBlank() || token == null || token.isBlank()) return false;
        return (" " + haystack + " ").contains(" " + token + " ");
    }

    private String normalize(String value) {
        if (value == null) return "";
        return value
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
