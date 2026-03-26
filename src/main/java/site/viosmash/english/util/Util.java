package site.viosmash.english.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.entity.User;

@Component
public class Util {

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public <T> PageResponse<T> convert(Page<T> page) {
        return PageResponse.of(page.getNumber() + 1, page.getSize(), page.getTotalPages(), page.getContent());
    }
}
