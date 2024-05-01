package konkuk.aiku.repository;

import konkuk.aiku.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, Long> {
}
