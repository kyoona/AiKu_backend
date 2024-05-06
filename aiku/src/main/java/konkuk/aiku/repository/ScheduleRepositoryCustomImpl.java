package konkuk.aiku.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<UserSchedule> findByUserIdAndScheduleId(Long userId, Long scheduleId) {
        String jpql = "SELECT us FROM UserSchedule us " +
                "WHERE us.user.id = :userId AND us.schedule.id = :scheduleId";
        TypedQuery<UserSchedule> query = entityManager.createQuery(jpql, UserSchedule.class)
                .setParameter("userId", userId)
                .setParameter("scheduleId", scheduleId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Users> findWaitUsersInSchedule(Long groupId, List<UserSchedule> acceptUsers) {
        List<Users> users = entityManager.createQuery(
                        "SELECT ug.user FROM UserGroup ug " +
                                "JOIN ug.user u " +
                                "WHERE u.id NOT IN (" +
                                "    SELECT us.user.id FROM UserSchedule us WHERE us IN :userList" +
                                ") " +
                                "AND ug.group.id = :groupId", Users.class)
                .setParameter("userList", acceptUsers)
                .setParameter("groupId", 1L)
                .getResultList();
        return users;
    }
}
