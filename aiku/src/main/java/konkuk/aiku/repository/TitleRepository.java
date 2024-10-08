package konkuk.aiku.repository;

import konkuk.aiku.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Long> {
    Optional<Title> findByTitleName(String titleName);
}
