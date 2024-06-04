package konkuk.aiku.repository;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.ResultType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BettingRepository extends JpaRepository<Betting, Long> {
    List<Betting> findBettingsByScheduleIdAndBettingType(Long scheduleId, BettingType bettingType);

    @Query("select b from Betting b join fetch b.targetUser tu where b.bettor.id = ?1 and b.schedule.id = ?2 and b.bettingType = ?3")
    Betting findBettingsWithTargetByUserIdAndScheduleIdAndBettingType(Long userId, Long scheduleId, BettingType bettingType);

    @Query("select b from Betting b where b.schedule.id = ?1 and b.bettor.id = ?2")
    List<Betting> findBettingsByScheduleIdAndBettorId(Long scheduleId, Long userId);

    @Query("select b from Betting b join fetch b.bettor join fetch b.targetUser join fetch b.schedule where b.id = ?1")
    Optional<Betting> findBettingWithUserAndSchedule(Long bettingId);

    @Query("select count(b) from Betting b where b.bettor.id = ?1 and b.resultType = ?2")
    long countByBettorIdAndResultType(Long userId, ResultType resultType);

    @Query("select count(b) from Betting b where b.targetUser.id = ?1 and b.resultType = ?2")
    long countByTargetUserIdAndResultType(Long userId, ResultType resultType);

    @Query("select b from Betting b where b.bettor.id = ?1 and b.schedule.group.id = ?2")
    List<Betting> findAllByBettorIdAndScheduleGroupId(Long userId, Long groupId);

    @Query("select b from Betting b where b.targetUser.id = ?1 and b.schedule.group.id = ?2")
    List<Betting> findAllByTargetUserIdAndScheduleGroupId(Long userId, Long groupId);
}
