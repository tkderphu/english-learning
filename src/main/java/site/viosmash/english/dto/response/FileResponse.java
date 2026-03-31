package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileResponse {
    private Integer id;
    private String fileName;
    private String originalName;
    private String contentType;
    private long size;
    private String path; // local storage path (internal)
    private String url;  // public URL to download via server
}
