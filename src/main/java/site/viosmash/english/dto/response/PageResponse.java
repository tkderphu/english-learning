package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private int page;
    private int limit;
    private long total;
    private List<T> data;
}
