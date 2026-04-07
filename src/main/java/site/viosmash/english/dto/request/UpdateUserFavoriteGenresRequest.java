package site.viosmash.english.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class UpdateUserFavoriteGenresRequest {
    private List<Integer> genreIds;
}
