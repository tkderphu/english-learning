package site.viosmash.english.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LessonCreateRequest {

    private String title;

    private Integer programId;

    private int order;

    private MultipartFile audio;
}