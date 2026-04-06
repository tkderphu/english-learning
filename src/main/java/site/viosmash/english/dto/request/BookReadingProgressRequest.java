package site.viosmash.english.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookReadingProgressRequest {

    @NotNull
    @Min(0)
    private Integer lastReadPageNumber;

    @NotNull
    @Min(1)
    private Integer totalPages;

    /**
     * Thời gian (giây) người dùng ở màn đọc trong lần gọi này; dùng để ghi nhật ký hoạt động nếu đủ lớn.
     */
    @Min(0)
    private Integer durationSeconds;
}
