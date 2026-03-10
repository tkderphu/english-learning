package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.UserTopicCreateRequest;
import site.viosmash.english.entity.UserTopic;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.UserTopicRepository;

@Service
@RequiredArgsConstructor
public class UserTopicService {

    private final UserTopicRepository userTopicRepository;

    public UserTopic create(UserTopicCreateRequest req) {
        UserTopic ut = new UserTopic();
        ut.setTopicId(req.getTopicId());
        ut.setUserId(req.getUserId());
        ut = userTopicRepository.save(ut);
        return ut;
    }
}
