package konkuk.aiku.repository;

import konkuk.aiku.domain.UserGroup;

import java.util.Optional;

public interface UserGroupRepositoryCustom {
    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);
    void deleteByUserIdAndGroupId(Long userId, Long groupId);
}
