package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.PageRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.service.PageService;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/page")
@RequiredArgsConstructor
@Tag(name = "Page", description = "Page endpoints")
public class PageController {

    private final PageService pageService;

    @Operation(summary = "Create a page (multipart/form-data)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody PageRequest req) {
        try {
            pageService.create(req);
            return ResponseEntity.ok(BaseResponse.success(null));
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create page: " + e.getMessage());
        }
    }
}
