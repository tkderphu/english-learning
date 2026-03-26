package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private int page;
    private int limit;
    private int total;
    private List<T> data;

    public static <T> PageResponse<T> of(int page, int limit, int total, List<T> data) {
        PageResponse<T> p = new PageResponse<>();
        p.page = page;
        p.limit = limit;
        p.total = total;
        p.data = data;
        return p;
    }
}
