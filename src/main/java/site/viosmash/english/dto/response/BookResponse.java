package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookResponse {
    private int id;
    private String title;
    private String language;
    private String coverUrl;
    private String genresName;
    private String authors;
    private int status;
    private List<ChapterResponse> chapters;


    public BookResponse(int id, String title, String language, String coverUrl, String genresName, String authors, int status) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.status = status;
    }
}
