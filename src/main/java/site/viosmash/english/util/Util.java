package site.viosmash.english.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.entity.User;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

@Component
public class Util {

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public <T> PageResponse<T> convert(Page<T> page) {
        return PageResponse.of(page.getNumber() + 1, page.getSize(), page.getTotalPages(), page.getContent());
    }

    public String getFileFormat(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.contains(".")) {
            return "unknown";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
