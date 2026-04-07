package site.viosmash.english.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class OpenAiTextRequest {
    private String model;
    private List<Map<String, Object>> input;
    private List<Map<String, Object>> messages;
    private Map<String, Object> text;
}