package site.viosmash.english.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PageRequest {

    private Integer chapterId;

    private List<String> content;
}
