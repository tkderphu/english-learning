package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextLookupResponse {
    private String selectedText;
    private String meaning;
    private String phonetic;
    private String audioUrl;
    private List<String> examples;
}
