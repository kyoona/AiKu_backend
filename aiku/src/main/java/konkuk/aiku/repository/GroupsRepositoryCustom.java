package konkuk.aiku.repository;

import konkuk.aiku.domain.UserGroup;

import java.util.Optional;

public interface GroupsRepositoryCustom {
    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);
    void deleteUserGroup(UserGroup userGroup);
}
