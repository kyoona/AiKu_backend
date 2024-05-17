package konkuk.aiku.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
    public Optional<UserGroup> findByUserAndGroup(Users user, Groups group) {
        String jpql = "SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId";
        TypedQuery<UserGroup> query = entityManager.createQuery(jpql, UserGroup.class)
                .setParameter("userId", user.getId())
                .setParameter("groupId", group.getId());
        return query.getResultList().stream().findFirst();
    }
}
