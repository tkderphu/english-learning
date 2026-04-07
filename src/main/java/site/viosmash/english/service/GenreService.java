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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    private final site.viosmash.english.repository.UserGenreRepository userGenreRepository;

    private final Util util;

    public List<GenreResponse> getList() {
        return genreRepository.findAllGenre();
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

    @org.springframework.transaction.annotation.Transactional
    public Boolean updateUserFavoriteGenres(site.viosmash.english.dto.request.UpdateUserFavoriteGenresRequest req) {
        Integer userId = util.getCurrentUser().getId();
        userGenreRepository.deleteByUserId(userId);
        if (req.getGenreIds() != null && !req.getGenreIds().isEmpty()) {
            java.util.List<site.viosmash.english.entity.UserGenre> genres = req.getGenreIds().stream().map(genreId -> {
                site.viosmash.english.entity.UserGenre ug = new site.viosmash.english.entity.UserGenre();
                ug.setUserId(userId);
                ug.setGenreId(genreId);
                return ug;
            }).collect(java.util.stream.Collectors.toList());
            userGenreRepository.saveAll(genres);
        }
        return true;
    }
}
