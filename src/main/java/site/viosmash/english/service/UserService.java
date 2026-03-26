package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.UserCreateRequest;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.dto.response.UserResponse;
import site.viosmash.english.dto.response.UserSessionResponse;
import site.viosmash.english.entity.User;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.UserRepository;
import site.viosmash.english.repository.UserSessionRepository;
import site.viosmash.english.util.Util;
import site.viosmash.english.util.enums.RoleType;
import site.viosmash.english.util.enums.Status;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    private final Util util;

    public User create(UserCreateRequest req) {

        boolean isPresent = userRepository
                .findByEmail(req.getEmail())
                .isPresent();

        if(isPresent) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Email đã tồn tại trong hệ thống");
        }

        User user = new User();
        user.setEmail(req.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());
        user.setRole(RoleType.USER.getValue());
        user.setStatus(Status.ACTIVE.getValue());

        this.userRepository.save(user);
        return user;
    }

    public PageResponse<UserResponse> getList(Pageable pageable, String keyword, Integer role, Integer status) {
        if(keyword != null) {
            keyword = "%" + keyword.toLowerCase() + "%";
        } else {
            keyword = "%%";
        }
        return util.convert(userRepository.findAllByKeyword(pageable, keyword, role  , status));
    }

    public PageResponse<UserSessionResponse> getListSession(Pageable pageable, String keyword, Integer status) {

        User currentUser = util.getCurrentUser();

        Integer userId = currentUser.getId();

        if (currentUser.getRole() == RoleType.ADMIN.getValue()) {
           userId = null;
        }

        if(keyword != null) {
            keyword = "%" + keyword.toLowerCase() + "%";
        } else {
            keyword = "%%";
        }
        return util.convert(userSessionRepository.findAllByKeyword(pageable, keyword, userId, status));
    }
}