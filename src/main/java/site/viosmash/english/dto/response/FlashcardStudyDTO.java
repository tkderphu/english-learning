package site.viosmash.english.dto.response;

public record FlashcardStudyDTO(
        Integer id,
        String term,
        String definition,
        String imageUrl
) {}
