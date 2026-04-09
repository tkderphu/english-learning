package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.response.FileResponse;
import site.viosmash.english.entity.FileEntity;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileRepository fileRepository;

    @Value("${file.storage.path:uploads}")
    private String storagePath;

    @Value("${app.server.base-url}")
    private String baseUrl;

    public FileResponse store(MultipartFile file) {
        try {
            // ensure directory exists
            Path baseDir = Path.of(storagePath).toAbsolutePath();
            Files.createDirectories(baseDir);

            String generated = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = baseDir.resolve(generated);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            FileEntity fe = new FileEntity();
            fe.setFileName(generated);
            fe.setOriginalName(file.getOriginalFilename());
            fe.setContentType(file.getContentType());
            fe.setSize(file.getSize());
            fe.setPath(target.toString());
            fe.setStatus(1);

            FileEntity saved = fileRepository.save(fe);

            // Public URL using static resource mapping (see WebMvcConfig)
            // We expose files under the /uploads/{filename} path regardless of whether storagePath is
            // relative or absolute. This keeps URLs stable for clients.
            String publicUrl = String.format("%s/api/assets/%s", baseUrl, saved.getFileName());

            return new FileResponse(saved.getId(), saved.getFileName(), saved.getOriginalName(), saved.getContentType(), saved.getSize(), saved.getPath(), publicUrl);
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file: " + e.getMessage());
        }
    }

    public Path loadPath(Integer id) {
        Optional<FileEntity> opt = fileRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "File not found");
        }
        return Path.of(opt.get().getPath());
    }

    public void delete(Integer id) {
        Optional<FileEntity> opt = fileRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "File not found");
        }

        FileEntity fe = opt.get();
        try {
            Path p = Path.of(fe.getPath());
            Files.deleteIfExists(p);
        } catch (IOException e) {
            // log and continue to delete metadata
        }

        fileRepository.deleteById(id);
    }
}
