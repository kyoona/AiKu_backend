package konkuk.aiku.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import konkuk.aiku.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{

    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    QSchedule qSchedule = QSchedule.schedule;
    QUserSchedule qUserSchedule = QUserSchedule.userSchedule;
    QUsers qUser = QUsers.users;

    @Override
    public Optional<UserSchedule> findUserScheduleByUserIdAndScheduleId(Long userId, Long scheduleId) {
        String jpql = "SELECT us FROM UserSchedule us " +
                "WHERE us.user.id = :userId AND us.schedule.id = :scheduleId";
        TypedQuery<UserSchedule> query = entityManager.createQuery(jpql, UserSchedule.class)
                .setParameter("userId", userId)
                .setParameter("scheduleId", scheduleId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<UserArrivalData> findUserArrivalDataByUserIdAndScheduleId(Long userId, Long scheduleId) {
        String jpql = "SELECT uad FROM UserArrivalData uad " +
                "WHERE uad.user.id = :userId AND uad.schedule.id = :scheduleId";
        TypedQuery<UserArrivalData> query = entityManager.createQuery(jpql, UserArrivalData.class)
                .setParameter("userId", userId)
                .setParameter("scheduleId", scheduleId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<UserArrivalData> findUserArrivalDatasWithUserByGroupId(Long groupId) {
        return entityManager.createQuery(
                        "SELECT uad" +
                                " FROM UserArrivalData uad" +
                                " JOIN FETCH uad.user u" +
                                " WHERE uad.group.id = :groupId", UserArrivalData.class
                )
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public List<UserArrivalData> findUserArrivalDatasWithUserByScheduleId(Long scheduleId) {
        return entityManager.createQuery(
                        "SELECT uad" +
                                " FROM UserArrivalData uad" +
                                " JOIN FETCH uad.user u" +
                                " WHERE uad.schedule.id = :scheduleId", UserArrivalData.class
                )
                .setParameter("scheduleId", scheduleId)
                .getResultList();
    }

    @Override
    public Optional<Schedule> findScheduleWithUser(Long scheduleId) {
        return entityManager.createQuery(
                "SELECT s" +
                        " From Schedule s" +
                        " JOIN FETCH s.users us" +
                        " JOIN FETCH us.user" +
                        " WHERE s.id = :scheduleId", Schedule.class
        )
                .setParameter("scheduleId", scheduleId)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<Schedule> findScheduleWithArrivalData(Long scheduleId) {
        return entityManager.createQuery(
                "SELECT s" +
                        " FROM Schedule s" +
                        " LEFT JOIN FETCH s.userArrivalDatas uad" +
                        " WHERE s.id = :scheduleId", Schedule.class
        )
                .setParameter("scheduleId", scheduleId)
                .getResultList().stream().findFirst();
    }

    @Override
    public List<Schedule> findScheduleWithUserByGroupId(Long groupId, String startTime, String endTime, ScheduleStatus status) {
        return jpaQueryFactory
                .select(qSchedule)
                .from(qSchedule)
                .join(qSchedule.users, qUserSchedule).fetchJoin()
                .join(qUserSchedule.user, qUser).fetchJoin()
                .where(
                        qSchedule.group.id.eq(groupId),
                        dateAfter(startTime,0),
                        dateBefore(endTime, 0),
                        statusEq(status, 0)
                ).orderBy(qSchedule.scheduleTime.desc())
                .fetch();
    }

    @Override
    public List<UserSchedule> findUserScheduleByUserId(Long userId, String startTime, String endTime, ScheduleStatus status) {
        return jpaQueryFactory
                .select(qUserSchedule)
                .from(qUserSchedule)
                .leftJoin(qUserSchedule.schedule, qSchedule).fetchJoin()
                .where(
                        qUserSchedule.user.id.eq(userId),
                        dateAfter(startTime, 1),
                        dateBefore(endTime, 1),
                        statusEq(status, 1)
                ).orderBy(qSchedule.scheduleTime.desc())
                .fetch();
    }

    private BooleanExpression dateAfter(String startDate, int type) {
        if (startDate == null) {
            return null;
        }
        if (type == 0) return qSchedule.scheduleTime.after(LocalDateTime.parse(startDate));
        else return qUserSchedule.schedule.scheduleTime.after(LocalDateTime.parse(startDate));
    }

    private BooleanExpression dateBefore(String endDate, int type) {
        if (endDate == null) {
            return null;
        }
        if (type == 0) return qSchedule.scheduleTime.before(LocalDateTime.parse(endDate));
        else return qUserSchedule.schedule.scheduleTime.before(LocalDateTime.parse(endDate));
    }

    private BooleanExpression statusEq(ScheduleStatus status, int type) {
        if (status == null) {
            return null;
        }
        if (type == 0) return qSchedule.status.eq(status);
        else return qUserSchedule.schedule.status.eq(status);
    }


}
