package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.AuthorCreateRequest;
import site.viosmash.english.dto.response.AuthorResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.entity.Author;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import site.viosmash.english.util.Util;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final Util util;

    public PageResponse<AuthorResponse> page(int page, int limit, String keyword) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        return util.convert(authorRepository.findAllByKeyword(PageRequest.of(page - 1, limit), kw));
    }

    public AuthorResponse create(AuthorCreateRequest req) {
        Author a = new Author();
        a.setName(req.getName());
        a.setAvatar(req.getAvatar());
        a.setNationality(req.getNationality());
        a.setBiography(req.getBiography());
        a.setStatus(1);
        a = authorRepository.save(a);
        return new AuthorResponse(a.getId(), a.getName(), a.getAvatar(), a.getNationality(), a.getBiography(), a.getStatus());
    }
}
