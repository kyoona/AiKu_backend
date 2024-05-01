package konkuk.aiku.repository;

import konkuk.aiku.domain.userItem.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
}
