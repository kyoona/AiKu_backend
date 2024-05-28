package konkuk.aiku.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
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
