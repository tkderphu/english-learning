package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.Audio;

public interface AudioRepository extends JpaRepository<Audio, Integer> {
}
