package site.viosmash.english.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.UserCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/v1")
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest req) {
        return ResponseEntity.ok(BaseResponse.success(userService.create(req)));
    }


    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(@PageableDefault()Pageable pageable,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Integer role,
                                                      @RequestParam(required = false) Integer status) {
        return ResponseEntity.ok(BaseResponse.success(userService.getList(pageable, keyword, role, status)));
    }
}
