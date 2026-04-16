package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class BookResponse {
    private Integer id;
    private String title;
    private String language;
    private String coverUrl;
    private String genresName;
    private String authors;
    private Integer status;
    private List<ChapterResponse> chapters;

    private Integer lastReadNumberPage;
    private Double progressPercent;
    private Date lastReadTime;
    private boolean isFavorite;

    public BookResponse(int id, String title, String language, String coverUrl, String genresName, String authors, Integer status) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.status = status;
    }

    public BookResponse(Number id, String title, String language, String coverUrl, String genresName, String authors, Number status, Number isFavorite) {
        this.id = id == null ? null : id.intValue();
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.status = status == null ? null : status.intValue();
        this.isFavorite = isFavorite != null && isFavorite.intValue() != 0;
    }

    public BookResponse(Number id, String title, String language, String coverUrl, String genresName, String authors, Number lastReadNumberPage, Number progressPercent, Date lastReadTime, Number isFavorite) {
        this.id = id == null ? null : id.intValue();
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.lastReadNumberPage = lastReadNumberPage == null ? null : lastReadNumberPage.intValue();
        this.progressPercent = progressPercent == null ? null : progressPercent.doubleValue();
        this.lastReadTime = lastReadTime;
        this.isFavorite = isFavorite != null && isFavorite.intValue() != 0;
    }
}
