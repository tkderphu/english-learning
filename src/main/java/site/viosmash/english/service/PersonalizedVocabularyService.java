package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String candidate = w.trim();
            if (isCoveredByChosen(candidate, chosen)) {
                continue;
            }
            chosen.add(candidate);
            if (chosen.size() >= MAX_PERSONAL_WORDS_PER_TURN) {
                break;
            }
        }

        return new ArrayList<>(chosen);
    }

    /** Cùng nguồn SQL với cá nhân hóa: tối đa 20 term flashcard gần nhất của user (canonical casing). */
    public List<String> listFlashcardTermsForUser(Integer userId) {
        if (userId == null) {
            return List.of();
        }
        return new ArrayList<>(loadFlashcardTerms(userId));
    }

    /**
     * Bọc HTML bold mọi cụm trùng term flashcard xuất hiện trong reply (không chỉ 4 từ chọn cho prompt),
     * chỗ chưa nằm trong {@code <b>…</b>}; hỗ trợ FE hiển thị in đậm / tô màu.
     */
    public String ensureFlashcardTermsBoldHtml(String reply, List<String> flashcardTerms) {
        if (reply == null || reply.isBlank() || flashcardTerms == null || flashcardTerms.isEmpty()) {
            return reply;
        }
        String s = markdownBoldToHtml(reply.trim());
        List<String> sorted = distinctTermsLongestFirst(flashcardTerms);
        for (String term : sorted) {
            Pattern p = Pattern.compile("(?i)(?<![A-Za-z0-9])" + Pattern.quote(term) + "(?![A-Za-z0-9])");
            Matcher m = p.matcher(s);
            List<int[]> ranges = new ArrayList<>();
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                if (!insideBoldTag(s, start)) {
                    ranges.add(new int[] { start, end });
                }
            }
            ranges.sort((a, b) -> Integer.compare(b[0], a[0]));
            for (int[] r : ranges) {
                String inner = s.substring(r[0], r[1]);
                s = s.substring(0, r[0]) + "<b>" + inner + "</b>" + s.substring(r[1]);
            }
        }
        return s;
    }

    private static List<String> distinctTermsLongestFirst(List<String> terms) {
        Map<String, String> byLower = new LinkedHashMap<>();
        for (String t : terms) {
            if (t == null) {
                continue;
            }
            String trim = t.trim();
            if (trim.length() < 2) {
                continue;
            }
            byLower.putIfAbsent(trim.toLowerCase(Locale.ROOT), trim);
        }
        List<String> out = new ArrayList<>(byLower.values());
        out.sort(Comparator.comparingInt(String::length).reversed());
        return out;
    }

    private static String markdownBoldToHtml(String raw) {
        return raw.replaceAll("(?s)\\*\\*(.+?)\\*\\*", "<b>$1</b>");
    }

    private static boolean insideBoldTag(String html, int index) {
        if (index <= 0) {
            return false;
        }
        int depth = 0;
        int i = 0;
        while (i < index && i < html.length()) {
            if (html.regionMatches(true, i, "<b>", 0, 3)) {
                depth++;
                i += 3;
            } else if (html.regionMatches(true, i, "</b>", 0, 4)) {
                depth = Math.max(0, depth - 1);
                i += 4;
            } else {
                i++;
            }
        }
        return depth > 0;
    }

    private boolean shouldInjectOnTurn(int turn) {
        return turn >= 2;
    }

    private boolean isCoveredByChosen(String candidate, Set<String> chosen) {
        String normalizedCandidate = normalize(candidate);
        if (normalizedCandidate.isBlank()) return true;
        for (String existing : chosen) {
            String normalizedExisting = normalize(existing);
            if (normalizedExisting.equals(normalizedCandidate)) return true;
            // Avoid choosing short subset term (coffee) when a fuller phrase already selected (black coffee).
            if (containsToken(normalizedExisting, normalizedCandidate) && normalizedExisting.length() > normalizedCandidate.length()) {
                return true;
            }
        }
        return false;
    }

    private int relevanceScore(String termValue, String latestMessage, String sessionContext) {
        int score = 0;
        String term = normalize(termValue);

        if (!term.isBlank() && containsToken(latestMessage, term)) score += 2;
        if (!term.isBlank() && containsToken(sessionContext, term)) score += 2;
        if (!term.isBlank() && term.contains(" ")) score += 1;
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

    private List<String> loadFlashcardTerms(Integer userId) {
        var params = new MapSqlParameterSource("userId", userId);
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
