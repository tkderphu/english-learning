package site.viosmash.english.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class BookCreateRequest {
    private String title;
    private String language;
    private String coverUrl;
    private List<Integer> authorIds;
    private List<Integer> genreIds;
}
