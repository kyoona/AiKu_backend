package konkuk.aiku.repository;

import konkuk.aiku.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);
    Long deleteByUserIdAndGroupId(Long userId, Long groupId);
    List<UserGroup> findByGroupId(Long groupId);
}
