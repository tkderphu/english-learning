package site.viosmash.english.dto.response;

public record FlashcardStudyDTO(
        Integer id,
        String word,
        String phonetic,
        String meaning,
        String exampleSentence,
        String visualCueUrl
) {}
