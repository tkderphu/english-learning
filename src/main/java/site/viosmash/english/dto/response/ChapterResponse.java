package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterResponse {
    private int id;
    private Integer bookId;
    private String title;
    private String description;
    private int number;
    private Number totalPages;
    private Number totalDuration;
}
