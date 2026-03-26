package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.GenreCreateRequest;
import site.viosmash.english.dto.response.GenreResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.entity.Genre;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.GenreRepository;
import site.viosmash.english.util.Util;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    private final Util util;

    public PageResponse<GenreResponse> page(int page, int limit, String keyword) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        return util.convert(genreRepository.findAllByKeyword(PageRequest.of(page - 1, limit), kw));
    }

    public GenreResponse create(GenreCreateRequest req) {
        Genre g = new Genre();
        g.setName(req.getName());
        g.setThumbnail(req.getThumbnail());
        g.setDescription(req.getDescription());
        g.setStatus(1);
        g = genreRepository.save(g);
        return new GenreResponse(g.getId(), g.getName(), g.getThumbnail(), g.getDescription(), g.getStatus());
    }
}
