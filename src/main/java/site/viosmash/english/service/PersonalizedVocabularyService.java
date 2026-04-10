package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;
import site.viosmash.english.entity.UserLearnedWord;
import site.viosmash.english.repository.UserLearnedWordRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonalizedVocabularyService {

    private static final int LOOKBACK_MESSAGES_FOR_REPEAT = 4;
    private static final int MAX_PERSONAL_WORDS_PER_TURN = 4;

    private final UserLearnedWordRepository userLearnedWordRepository;

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

        List<UserLearnedWord> recentWords = userLearnedWordRepository.findTop20ByUserIdOrderByCreatedAtDesc(userId);
        if (recentWords == null || recentWords.isEmpty()) {
            return List.of();
        }

        String latestNormalized = normalize(latestUserMessage);
        String sessionContext = normalize(
                safe(session.getTitle()) + " " + safe(session.getInstructionSnapshot()) + " " + safe(session.getAiRoleSnapshot())
        );
        String recentConversation = normalizeRecentConversation(recentMessages);

        List<UserLearnedWord> filtered = recentWords.stream()
                .filter(w -> w.getTerm() != null && !w.getTerm().isBlank())
                .filter(w -> !isRepeatedRecently(w.getTerm(), recentConversation))
                .sorted(Comparator
                        .comparingInt((UserLearnedWord w) -> relevanceScore(w, latestNormalized, sessionContext))
                        .reversed()
                        .thenComparing(UserLearnedWord::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        // Fallback: if repeat-filter is too strict, keep personalization alive with top recent words.
        if (filtered.isEmpty()) {
            filtered = recentWords.stream()
                    .filter(w -> w.getTerm() != null && !w.getTerm().isBlank())
                    .sorted(Comparator
                            .comparingInt((UserLearnedWord w) -> relevanceScore(w, latestNormalized, sessionContext))
                            .reversed()
                            .thenComparing(UserLearnedWord::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
        }

        Set<String> chosen = new LinkedHashSet<>();
        for (UserLearnedWord w : filtered) {
            chosen.add(w.getTerm().trim());
            if (chosen.size() >= MAX_PERSONAL_WORDS_PER_TURN) {
                break;
            }
        }

        return new ArrayList<>(chosen);
    }

    private boolean shouldInjectOnTurn(int turn) {
        // Start from learner turn 2 so personalization appears earlier and more consistently.
        return turn >= 2;
    }

    private int relevanceScore(UserLearnedWord w, String latestMessage, String sessionContext) {
        int score = 0;
        String term = normalize(w.getTerm());
        String definition = normalize(w.getDefinition());

        if (!term.isBlank() && containsToken(latestMessage, term)) {
            score += 2;
        }
        if (!term.isBlank() && containsToken(sessionContext, term)) {
            score += 2;
        }
        if (!definition.isBlank() && hasAnyOverlap(definition, latestMessage)) {
            score += 1;
        }
        if (Boolean.TRUE.equals(w.getFavorite())) {
            score += 1;
        }
        if (Boolean.TRUE.equals(w.getNeedsAttention())) {
            score += 1;
        }
        return score;
    }

    private boolean isRepeatedRecently(String term, String recentConversation) {
        return containsToken(recentConversation, normalize(term));
    }

    private String normalizeRecentConversation(List<AiChatMessage> recentMessages) {
        if (recentMessages == null || recentMessages.isEmpty()) {
            return "";
        }
        int from = Math.max(0, recentMessages.size() - LOOKBACK_MESSAGES_FOR_REPEAT);
        StringBuilder sb = new StringBuilder();
        for (AiChatMessage m : recentMessages.subList(from, recentMessages.size())) {
            sb.append(' ').append(safe(m.getContent()));
        }
        return normalize(sb.toString());
    }

    private boolean hasAnyOverlap(String left, String right) {
        if (left.isBlank() || right.isBlank()) {
            return false;
        }
        Set<String> leftTokens = tokenize(left);
        if (leftTokens.isEmpty()) {
            return false;
        }
        for (String token : tokenize(right)) {
            if (leftTokens.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> tokenize(String text) {
        if (text == null || text.isBlank()) {
            return Set.of();
        }
        String[] parts = normalize(text).split("\\s+");
        Set<String> tokens = new LinkedHashSet<>();
        for (String p : parts) {
            if (p.length() >= 3) {
                tokens.add(p);
            }
        }
        return tokens;
    }

    private boolean containsToken(String haystack, String token) {
        if (haystack == null || haystack.isBlank() || token == null || token.isBlank()) {
            return false;
        }
        return (" " + haystack + " ").contains(" " + token + " ");
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
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
