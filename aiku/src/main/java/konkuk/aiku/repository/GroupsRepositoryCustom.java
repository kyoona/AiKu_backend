package konkuk.aiku.repository;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;

import java.util.Optional;

public interface GroupsRepositoryCustom {
    Optional<UserGroup> findByUserAndGroup(Users user, Groups group);
    void deleteUserGroup(UserGroup userGroup);
}
