package konkuk.aiku.repository;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingRepository extends JpaRepository<Betting, Long> {
    List<Betting> findBettingsByScheduleIdAndBettingType(Long scheduleId, BettingType bettingType);
}
