package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.TopicCreateRequest;
import site.viosmash.english.dto.response.TopicResponse;
import site.viosmash.english.entity.Topic;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.TopicRepository;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public Page<TopicResponse> page(int page, int limit, String keyword) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        return topicRepository.findAllByKeyword(PageRequest.of(page - 1, limit), kw);
    }

    public TopicResponse create(TopicCreateRequest req) {
        Topic t = new Topic();
        t.setName(req.getName());
        t.setDescription(req.getDescription());
        t.setThumbnail(req.getThumbnail());
        t.setStatus(1);
        t = topicRepository.save(t);
        return new TopicResponse(t.getId(), t.getName(), t.getDescription(), t.getThumbnail(), t.getStatus());
    }
}
