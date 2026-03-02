package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
    private Integer id;
    private String fileName;
    private String originalName;
    private String contentType;
    private long size;
    private String path; // local storage path (internal)
    private String url;  // public URL to download via server
}
