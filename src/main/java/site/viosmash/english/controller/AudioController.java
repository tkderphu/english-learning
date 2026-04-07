package site.viosmash.english.controller;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.response.AudioResponse;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.FileResponse;
import site.viosmash.english.entity.Audio;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.AudioRepository;
import site.viosmash.english.service.FileStorageService;
import site.viosmash.english.util.Util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioController {

    private final FileStorageService fileStorageService;
    private final AudioRepository audioRepository;
    private final Util util;

    @PostMapping("/v1/upload")
    public ResponseEntity<BaseResponse<?>> upload(@RequestParam("file") MultipartFile file) {
        try {
            FileResponse stored = fileStorageService.store(file);

            // read audio metadata from the stored file on disk to avoid InputStream mark/reset issues
            java.io.File storedFile = java.nio.file.Path.of(stored.getPath()).toFile();

            Mp3File mp3 = new Mp3File(storedFile);

            Audio audio = new Audio();
            audio.setDuration(mp3.getLengthInMilliseconds());
            audio.setSampleRate(mp3.getSampleRate());
            audio.setFileSize(stored.getSize());
            audio.setFileUrl(stored.getUrl());
            audio.setFormat(util.getFileFormat(file));

            Audio saved = audioRepository.save(audio);

            AudioResponse resp = new AudioResponse(saved.getId(), saved.getDuration(), saved.getFormat(), saved.getSampleRate(), saved.getFileSize(), saved.getFileUrl());

            return ResponseEntity.ok(BaseResponse.success(resp));
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process audio: " + e.getMessage());
        } catch (InvalidDataException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedTagException e) {
            throw new RuntimeException(e);
        }
    }
}
