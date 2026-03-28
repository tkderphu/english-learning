package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckResponse {
    private int id;
    private Integer userId;
    private String title;
    private String coverImageUrl;
    private Integer totalWords;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private java.util.List<FlashcardResponse> flashcards;
}
