package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.event.BettingEventPublisher;
import konkuk.aiku.event.UserPointEventPublisher;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.BettingRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserPointRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.BettingServiceDto;
import konkuk.aiku.service.dto.UserBettingResultServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BettingService {

    private final BettingRepository bettingRepository;
    private final ScheduleRepository scheduleRepository;
    private final UsersRepository usersRepository;
    private final UserPointRepository userPointRepository;
    private final UserPointEventPublisher userPointEventPublisher;
    private final BettingEventPublisher bettingEventPublisher;


    private Optional<UserSchedule> findUserInSchedule(Long userId, Long scheduleId) {
        return scheduleRepository.findUserScheduleByUserIdAndScheduleId(userId, scheduleId);
    }

    private Users findUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_USER));
    }

    private Betting findBettingById(Long bettingId) {
        return bettingRepository.findById(bettingId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_BETTING));
    }

    public BettingServiceDto findBetting(Long bettingId) {
        Betting betting = findBettingById(bettingId);

        return BettingServiceDto.toServiceDto(betting);
    }

    public Long addBetting(Users users, Long scheduleId, BettingServiceDto bettingServiceDto) {
        Optional<UserSchedule> userInSchedule = findUserInSchedule(users.getId(), scheduleId);

        if (userInSchedule.isEmpty()) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        Users targetUser = usersRepository.findById(bettingServiceDto.getTargetUser().getUserId())
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_USER));

        UserSchedule userSchedule = userInSchedule.get();
        Schedule schedule = userSchedule.getSchedule();

        Betting betting = Betting.builder()
                .bettor(users)
                .targetUser(targetUser)
                .point(bettingServiceDto.getPoint())
                .schedule(schedule)
                .bettingType(bettingServiceDto.getBettingType())
                .bettingStatus(BettingStatus.WAIT)
                .build();

        bettingRepository.save(betting);

        // 1대1 레이싱인 경우
        if (betting.getBettingType().equals(BettingType.RACING)) {
            // 베팅 상대에게 알림 메시지 (현재는 레이싱 시작 메시지로 보냄)
            // 1분 대기 스케줄러에 추가
            bettingEventPublisher.racingApplyEvent(scheduleId, betting.getId());
        } else {
            // 베팅인 경우
            // 베팅 금액 지불
            userPointEventPublisher.userPointChangeEvent(users.getId(), betting.getPoint(), PointType.BETTING, PointChangeType.MINUS, LocalDateTime.now());
            betting.setBettingStatus(BettingStatus.ACCEPT);
            schedule.addBetting(betting);
        }

        return betting.getId();
    }

    public Long acceptBetting(Long bettingId) {
        Betting betting = findBettingById(bettingId);
        betting.setBettingStatus(BettingStatus.ACCEPT);

        Users bettor = betting.getBettor();
        Users targetUser = betting.getTargetUser();

        // 베팅 거는 비용
        int point = betting.getPoint();

        userPointEventPublisher.userPointChangeEvent(bettor.getId(), point, PointType.BETTING, PointChangeType.MINUS, LocalDateTime.now());
        userPointEventPublisher.userPointChangeEvent(targetUser.getId(), point, PointType.BETTING, PointChangeType.MINUS, LocalDateTime.now());

        // schedule에 레이싱 추가
        betting.getSchedule().addRacing(betting);

        // 베팅 주인에게 수락 알림 메시지
        bettingEventPublisher.racingAcceptEvent(betting.getSchedule().getId(), bettingId);

        return betting.getId();
    }

    // 레이싱 미수락 시 베팅 삭제 로직
    public Runnable deleteBettingById(Long bettingId) {
        return () -> {
            Betting betting = findBettingById(bettingId);
            // 베팅이 수락되지 않은 경우
            if (!betting.getBettingStatus().equals(BettingStatus.ACCEPT)) {
                bettingEventPublisher.racingDenyEvent(betting.getSchedule().getId(), bettingId);
                // 건 금액 돌려주기
                userPointEventPublisher.userPointChangeEvent(betting.getBettor().getId(), betting.getPoint(), PointType.BETTING, PointChangeType.PLUS, LocalDateTime.now());
                bettingRepository.deleteById(bettingId);
            }
        };
    }


    // 베팅 업데이트 로직 미사용(Deprecated)
    public Long updateBetting(Users users, Long scheduleId, Long bettingId, BettingServiceDto bettingServiceDto) {
        Optional<UserSchedule> userInSchedule = findUserInSchedule(users.getId(), scheduleId);

        if (userInSchedule.isEmpty()) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        // Schedule 검증
        Betting betting = findBettingById(bettingId);
        Users target = findUserById(bettingServiceDto.getTargetUser().getUserId());

        betting.setTargetUser(target);
        betting.setPoint(bettingServiceDto.getPoint());

        return betting.getId();
    }

    public Long deleteBetting(Users users, Long scheduleId, Long bettingId) {
        Optional<UserSchedule> userInSchedule = findUserInSchedule(users.getId(), scheduleId);

        if (userInSchedule.isEmpty()) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        // Schedule 검증
        bettingRepository.deleteById(bettingId);

        return bettingId;
    }

    public List<BettingServiceDto> getBettingsByType(Long scheduleId, BettingType bettingType) {
        List<Betting> bettings = bettingRepository.findBettingsByScheduleIdAndBettingType(scheduleId, bettingType);

        return bettings.stream()
                .map(BettingServiceDto::toServiceDto)
                .collect(Collectors.toList());
    }

    /**
     * 이벤트 발생 메서드
     * 스케줄 종료시 레이싱 결과 생성 로직
     * @param scheduleId 도착한 유저가 속한 스케줄
     * @return 유저 아이디
     */
    public Long userRacingArrival(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE));

        List<Betting> racings = schedule.getRacings();

        for (Betting racing : racings) {
            // 도착 유저에 대해서 아직 종료되지 않은 레이싱만 실행
            if (racing.getBettor().getId() == userId && racing.getBettingStatus().equals(BettingStatus.ACCEPT)) {
                setRacingResult(racing, ResultType.WIN);
            }
            else if (racing.getTargetUser().getId() == userId && racing.getBettingStatus().equals(BettingStatus.ACCEPT)) {
                setRacingResult(racing, ResultType.LOSE);
            }
        }

        return userId;
    }

    public void setRacingResult(Betting betting, ResultType resultType) {
        int point = betting.getPoint();

        // 플러스 포인트 : 베팅 걸린 시점에서 베팅 포인트 가져갔기 때문에 2배로 더해준다.
        int plusPoint = point * 2;

        Long winnerId = resultType.equals(ResultType.WIN) ? betting.getBettor().getId() : betting.getTargetUser().getId();

        userPointEventPublisher.userPointChangeEvent(winnerId, plusPoint, PointType.BETTING, PointChangeType.PLUS, LocalDateTime.now());
        bettingEventPublisher.racingEndEvent(betting.getSchedule().getId(), betting.getId());

        betting.updateBettingResult(resultType);
        betting.setBettingStatus(BettingStatus.DONE);
    }

    /**
     * 이벤트 발생 메서드
     * 스케줄 종료시 베팅 & 레이싱 결과 생성 로직
     * @return 해당 스케줄 아이디
     */
    public Long setAllBettings(Long scheduleId) {
        // 베팅 결과 설정
        List<Betting> bettings = bettingRepository.findBettingsByScheduleIdAndBettingType(scheduleId, BettingType.BETTING);
        for (Betting betting : bettings) {
            setBettingResult(betting);
        }
        // 베팅 전체 결과에 따른 포인트 분배
        bettingPointCalculate(bettings);

        // 둘 다 지각한 경우에 대한 레이싱 로직
        List<Betting> racings = bettingRepository.findBettingsByScheduleIdAndBettingType(scheduleId, BettingType.RACING);
        for (Betting racing : racings) {
            // 아직 레이싱이 종료되지 않은 레이싱에 대해 실행
            if (racing.getBettingStatus().equals(BettingStatus.ACCEPT)) {
                setDrawRacing(racing);
            }
        }
        return scheduleId;
    }

    public void setBettingResult(Betting betting) {
        Users targetUser = betting.getTargetUser();

        Schedule schedule = betting.getSchedule();
        // 유저 도착 정보 정렬
        List<UserArrivalData> userArrivalDatas = schedule.getUserArrivalDatas();
        userArrivalDatas.sort(Comparator.comparing(UserArrivalData::getArrivalTime));
        UserArrivalData lastArrivalData = userArrivalDatas.get(userArrivalDatas.size() - 1);

        ResultType resultType;

        if (lastArrivalData.getUser().getId() == targetUser.getId()) {
            // 베팅 성공!
            resultType = ResultType.WIN;
        } else {
            // 베팅 실패..
            resultType = ResultType.LOSE;
        }

        betting.updateBettingResult(resultType);
        betting.setBettingStatus(BettingStatus.DONE);

    }

    public void bettingPointCalculate(List<Betting> bettings) {
        int bettingPoints = 0;
        int winners = 0;
        for (Betting betting : bettings) {
            bettingPoints += betting.getPoint();
            if (betting.getResultType().equals(ResultType.WIN)) {
                winners += 1;
            }
        }

        int reward = 0;
        if (winners != 0) {
            reward = bettingPoints / winners;
        }

        for (Betting betting : bettings) {
            if (betting.getResultType().equals(ResultType.WIN)) {
                userPointEventPublisher.userPointChangeEvent(betting.getBettor().getId(), reward, PointType.BETTING, PointChangeType.PLUS, LocalDateTime.now());
            }
        }

    }

    public void setDrawRacing(Betting betting) {
        int point = betting.getPoint();
        betting.setResultType(ResultType.DRAW);
        betting.setBettingStatus(BettingStatus.DONE);
        // 베팅 건 금액 돌려주기
        Users bettor = betting.getBettor();
        Users targetUser = betting.getTargetUser();

        userPointEventPublisher.userPointChangeEvent(bettor.getId(), point, PointType.BETTING, PointChangeType.PLUS, LocalDateTime.now());
        userPointEventPublisher.userPointChangeEvent(targetUser.getId(), point, PointType.BETTING, PointChangeType.PLUS, LocalDateTime.now());

    }

    public List<UserBettingResultServiceDto> getBettingResult(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE));

        List<Users> users = schedule.getUsers().stream()
                .map(s -> s.getUser())
                .collect(Collectors.toList());

        List<UserBettingResultServiceDto> userProfitList = new ArrayList<>();

        for (Users user : users) {
            userProfitList.add(
                    new UserBettingResultServiceDto(user.getId(), user.getUsername(),
                            user.getUserImg(), user.getUserImgData(), 0)
            );
        }

        LocalDateTime scheduleStartTime = schedule.getScheduleTime();
        List<UserArrivalData> userArrivalData = schedule.getUserArrivalDatas()
                .stream()
                .sorted(Comparator.comparing(UserArrivalData::getTimeDifference))
                .collect(Collectors.toList());

        LocalDateTime scheduleEndTime = userArrivalData.get(0).getArrivalTime();

        for (UserBettingResultServiceDto userBetting : userProfitList) {
            List<UserPoint> userPoints = userPointRepository.findAllByUserIdAndCreatedAtBetween(userBetting.getUserId(), scheduleStartTime, scheduleEndTime);
            for (UserPoint userPoint : userPoints) {
                int addPoint = (userPoint.getPointChangeType().equals(PointChangeType.PLUS)) ? userPoint.getPoint() : userPoint.getPoint() * (-1);
                userBetting.addProfit(addPoint);
            }
        }

        return userProfitList;

    }
}
