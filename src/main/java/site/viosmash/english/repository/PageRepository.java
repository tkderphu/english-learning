package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {
}
