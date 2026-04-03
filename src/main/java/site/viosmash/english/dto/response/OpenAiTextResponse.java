package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiTextResponse {
    private String id;
    private List<OutputItem> output;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutputItem {
        private String type;
        private List<ContentItem> content;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentItem {
        private String type;
        private String text;
    }

    public String extractText() {
        if (output == null) return null;
        for (OutputItem out : output) {
            if (out.getContent() == null) continue;
            for (ContentItem c : out.getContent()) {
                if (c.getText() != null && !c.getText().isBlank()) {
                    return c.getText();
                }
            }
        }
        return null;
    }
}