package konkuk.aiku.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import konkuk.aiku.domain.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupsRepositoryCustomImpl implements GroupsRepositoryCustom{
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId) {
        String jpql = "SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId";
        TypedQuery<UserGroup> query = entityManager.createQuery(jpql, UserGroup.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteUserGroup(UserGroup userGroup) {
        entityManager.remove(userGroup);
    }
}
