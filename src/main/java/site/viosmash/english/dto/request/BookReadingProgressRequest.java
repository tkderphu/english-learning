package site.viosmash.english.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookReadingProgressRequest {

    @NotNull
    @Min(0)
    private Integer lastReadPageNumber;

    @NotNull
    private String lastRead;

    @NotNull
    @Min(0)
    private Integer duration;
}
