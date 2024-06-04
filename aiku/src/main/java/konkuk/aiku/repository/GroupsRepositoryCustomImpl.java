package konkuk.aiku.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupsRepositoryCustomImpl implements GroupsRepositoryCustom{

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<UserGroup> findByUserAndGroup(Users user, Long groupId) {
        return entityManager.createQuery(
                        "SELECT ug" +
                        " FROM UserGroup ug" +
                        " WHERE ug.user.id = :userId" +
                        " AND ug.group.id = :groupId", UserGroup.class)
                .setParameter("userId", user.getId())
                .setParameter("groupId", groupId)
                .getResultStream().findFirst();
    }

    @Override
    public Optional<Groups> findGroupWithUser(Long groupId) {
        return entityManager.createQuery(
                "SELECT g" +
                        " FROM Groups g" +
                        " JOIN FETCH g.userGroups ug" +
                        " JOIN FETCH ug.user u" +
                        " WHERE g.id = :groupId", Groups.class
        )
                .setParameter("groupId", groupId)
                .getResultList().stream().findFirst();
    }

}
