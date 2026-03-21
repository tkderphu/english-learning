package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class FavoriteRequest {
    private int bookId;
    private boolean isFavorite;
}
