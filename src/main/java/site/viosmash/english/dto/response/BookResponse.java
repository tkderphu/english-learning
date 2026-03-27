package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class BookResponse {
    private int id;
    private String title;
    private String language;
    private String coverUrl;
    private String genresName;
    private String authors;
    private int status;
    private List<ChapterResponse> chapters;
    private int lastReadNumberPage;
    private LocalDateTime lastReadTime;
    private double progressPercent;
    private boolean isFavorite;


    public BookResponse(int id, String title, String language, String coverUrl, String genresName, String authors, int status) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.status = status;
    }

    public BookResponse(int id, String title, String language, String coverUrl, String genresName, String authors, int lastReadNumberPage,double progressPercent, boolean isFavorite,LocalDateTime lastReadTime) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.lastReadNumberPage=lastReadNumberPage;
        this.progressPercent = progressPercent;
        this.lastReadTime = lastReadTime;
        this.isFavorite = isFavorite;
    }
}
