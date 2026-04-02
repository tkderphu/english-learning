package site.viosmash.english.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PageRequest {

    private Integer chapterId;

    private String content;

    private int audioId;

    private int number;
}
