package konkuk.aiku.repository;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;

import java.util.List;
import java.util.Optional;

public interface GroupsRepositoryCustom {
    Optional<UserGroup> findByUserAndGroup(Users user, Long groupId);
    Optional<Groups> findGroupWithUser(Long groupId);
}
