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

    public BookResponse(Integer id, String title, String language, String coverUrl, String genresName, String authors, Integer lastReadNumberPage, Double progressPercent, Date lastReadTime, Integer isFavorite) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.coverUrl = coverUrl;
        this.genresName = genresName;
        this.authors = authors;
        this.lastReadNumberPage = lastReadNumberPage;
        this.progressPercent = progressPercent;
        this.lastReadTime = lastReadTime;
        this.isFavorite = isFavorite == null || isFavorite == 0 ? false : true;
    }
}
