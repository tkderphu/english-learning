package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.file.Files;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.FileResponse;
import site.viosmash.english.service.FileStorageService;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Tag(name = "File", description = "File storage endpoints")
public class FileController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "Upload a file")
    @PostMapping("/v1/upload")
    public ResponseEntity<BaseResponse<?>> upload(@RequestParam("file") MultipartFile file) {
        FileResponse res = fileStorageService.store(file);
        return ResponseEntity.ok(BaseResponse.success(res));
    }

    @Operation(summary = "Delete a file by id")
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Integer id) {
        fileStorageService.delete(id);
        return ResponseEntity.ok(BaseResponse.success("Deleted"));
    }

    @Operation(summary = "Download file by id")
    @GetMapping("/v1/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Integer id) {
        var path = fileStorageService.loadPath(id);
        Resource resource = new PathResource(path);

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(path);
        } catch (Exception ignored) {}

        String filename = path.getFileName().toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
