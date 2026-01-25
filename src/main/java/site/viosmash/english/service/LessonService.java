package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.LessonCreateRequest;
import site.viosmash.english.entity.Lesson;
import site.viosmash.english.repository.LessonRepository;
import site.viosmash.english.util.Util;
import site.viosmash.english.util.enums.Status;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final Util util;
    private final LessonRepository lessonRepository;

    public Lesson create(LessonCreateRequest request) {

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setOrder(request.getOrder());
        lesson.setStatus(Status.ACTIVE.getValue());
        lesson.setProgramId(request.getProgramId());

        lessonRepository.save(lesson);
        return lesson;
    }
}


