package konkuk.aiku.repository;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.ResultType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingRepository extends JpaRepository<Betting, Long> {
    List<Betting> findBettingsByScheduleIdAndBettingType(Long scheduleId, BettingType bettingType);

    @Query("select b from Betting b where b.schedule.id = ?1 and b.bettor.id = ?2")
    List<Betting> findBettingsByScheduleIdAndBettorId(Long scheduleId, Long userId);

    @Query("select count(b) from Betting b where b.bettor.id = ?1 and b.resultType = ?2")
    long countByBettorIdAndResultType(Long userId, ResultType resultType);

    @Query("select count(b) from Betting b where b.targetUser.id = ?1 and b.resultType = ?2")
    long countByTargetUserIdAndResultType(Long userId, ResultType resultType);

}
