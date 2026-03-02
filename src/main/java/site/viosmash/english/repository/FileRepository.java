package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
}
