package konkuk.aiku.repository;

import konkuk.aiku.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    Long deleteByUserIdAndGroupId(Long userId, Long groupId);
}
