package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.request.LogLearningActivityRequest;
import site.viosmash.english.dto.request.ProfileUpdateRequest;
import site.viosmash.english.dto.request.UserLearnedWordCreateRequest;
import site.viosmash.english.dto.request.UserLearnedWordPatchRequest;
import site.viosmash.english.dto.response.ActivityDayDetailResponse;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.CorrectionSessionReviewResponse;
import site.viosmash.english.dto.response.HeatmapDayResponse;
import site.viosmash.english.dto.response.UserCorrectionItemResponse;
import site.viosmash.english.dto.response.LearningActivityItemResponse;
import site.viosmash.english.dto.response.LearningStatsOverviewResponse;
import site.viosmash.english.dto.response.ProfileMeResponse;
import site.viosmash.english.dto.response.UserLearnedWordResponse;
import site.viosmash.english.service.ProfileCorrectionService;
import site.viosmash.english.service.ProfileLearningActivityService;
import site.viosmash.english.service.ProfileService;
import site.viosmash.english.service.UserLearnedWordService;
import site.viosmash.english.util.Util;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/profile/v1", "/profile/v1"})
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Hồ sơ, thống kê học tập, heatmap, từ vựng")
public class ProfileController {

    private final Util util;
    private final ProfileService profileService;
    private final ProfileLearningActivityService learningActivityService;
    private final UserLearnedWordService userLearnedWordService;
    private final ProfileCorrectionService profileCorrectionService;

    @GetMapping("/me")
    @Operation(summary = "Xem thông tin cơ bản (email, tên, avatar, …)")
    public ResponseEntity<BaseResponse<ProfileMeResponse>> me() {
        return ResponseEntity.ok(BaseResponse.success(profileService.getProfile(util.getCurrentUser())));
    }

    @PatchMapping("/me")
    @Operation(summary = "Cập nhật tên hiển thị, location, level (email không đổi qua đây)")
    public ResponseEntity<BaseResponse<ProfileMeResponse>> updateMe(@Valid @RequestBody ProfileUpdateRequest req) {
        return ResponseEntity.ok(BaseResponse.success(profileService.updateProfile(util.getCurrentUser(), req)));
    }

    @PostMapping(value = "/me/avatar", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật ảnh đại diện (multipart file)")
    public ResponseEntity<BaseResponse<ProfileMeResponse>> avatar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(BaseResponse.success(profileService.updateAvatar(util.getCurrentUser(), file)));
    }

    @GetMapping("/stats/overview")
    @Operation(summary = "Thống kê tổng quan: bài hoàn thành, từ đã học, streak, tổng ngày học")
    public ResponseEntity<BaseResponse<LearningStatsOverviewResponse>> statsOverview() {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(learningActivityService.overview(userId)));
    }

    @GetMapping("/activity/heatmap")
    @Operation(summary = "Heatmap theo ngày (mức intensity 0–3). Mặc định ~365 ngày gần nhất nếu không truyền from/to")
    public ResponseEntity<BaseResponse<List<HeatmapDayResponse>>> heatmap(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(learningActivityService.heatmap(userId, from, to)));
    }

    @GetMapping("/activity/day/{date}")
    @Operation(summary = "Chi tiết hoạt động trong một ngày")
    public ResponseEntity<BaseResponse<ActivityDayDetailResponse>> dayDetail(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(learningActivityService.dayDetail(userId, date)));
    }

    @GetMapping("/activity/history")
    @Operation(summary = "Danh sách bài học / bài tập đã hoàn thành (LESSON, EXERCISE). filter=ALL|LESSON|EXERCISE")
    public ResponseEntity<BaseResponse<Page<LearningActivityItemResponse>>> activityHistory(
            @RequestParam(defaultValue = "ALL") String filter,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "startedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(learningActivityService.activityHistory(userId, filter, q, pageable)));
    }

    @PostMapping("/activity/log")
    @Operation(summary = "Ghi nhận một hoạt động học (app / module khác gọi sau khi hoàn thành)")
    public ResponseEntity<BaseResponse<LearningActivityItemResponse>> logActivity(
            @Valid @RequestBody LogLearningActivityRequest req
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(learningActivityService.logActivity(userId, req)));
    }

    @GetMapping("/vocabulary")
    @Operation(summary = "Danh sách từ đã học. filter=ALL|FAVORITES|NEEDS_ATTENTION; q=tìm theo term (chứa, không phân biệt hoa thường)")
    public ResponseEntity<BaseResponse<Page<UserLearnedWordResponse>>> vocabulary(
            @RequestParam(defaultValue = "ALL") String filter,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(userLearnedWordService.list(userId, filter, q, pageable)));
    }

    @PostMapping("/vocabulary")
    @Operation(summary = "Thêm từ vào danh sách đã học")
    public ResponseEntity<BaseResponse<UserLearnedWordResponse>> addVocabulary(
            @Valid @RequestBody UserLearnedWordCreateRequest req
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(userLearnedWordService.add(userId, req)));
    }

    @PatchMapping("/vocabulary/{id}")
    @Operation(summary = "Đánh dấu yêu thích / cần chú ý")
    public ResponseEntity<BaseResponse<UserLearnedWordResponse>> patchVocabulary(
            @PathVariable Integer id,
            @RequestBody UserLearnedWordPatchRequest req
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(userLearnedWordService.patch(userId, id, req)));
    }

    @GetMapping("/corrections")
    @Operation(summary = "Lỗi AI đã sửa (từ AI Chat), mới nhất trước. filter: ALL|GRAMMAR|VOCABULARY|SPELLING")
    public ResponseEntity<BaseResponse<Page<UserCorrectionItemResponse>>> corrections(
            @RequestParam(defaultValue = "ALL") String filter,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(profileCorrectionService.listCorrections(userId, filter, q, pageable)));
    }

    @GetMapping("/corrections/sessions/{sessionId}")
    @Operation(summary = "Chi tiết tất cả lỗi trong một phiên chat")
    public ResponseEntity<BaseResponse<CorrectionSessionReviewResponse>> correctionSession(
            @PathVariable Integer sessionId
    ) {
        int userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(profileCorrectionService.sessionReview(userId, sessionId)));
    }

    @DeleteMapping("/corrections")
    @Operation(summary = "Xóa toàn bộ lịch sử lỗi (bản ghi ai_message_errors của user)")
    public ResponseEntity<BaseResponse<Boolean>> clearCorrections() {
        int userId = util.getCurrentUser().getId();
        profileCorrectionService.clearHistory(userId);
        return ResponseEntity.ok(BaseResponse.success(true));
    }
}
