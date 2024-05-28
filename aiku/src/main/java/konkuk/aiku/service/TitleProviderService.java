package konkuk.aiku.service;


import konkuk.aiku.domain.*;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TitleProviderService {
    private final UsersRepository usersRepository;
    private final BettingRepository bettingRepository;
    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final ScheduleRepository scheduleRepository;

    private static final String 베팅_10회_패배 = "기부천사";
    private static final String 베팅_5회_승리 = "정세훈";
    private static final String 지각_5회_이상 = "못 말리는 아가씨";
    private static final String 일찍_도착_10번 = "최원탁";
    private static final String 일만_포인트_달성 = "만수르";

    /*
    베팅 총 10회 패배 : 기부천사
    베팅 5회 승리 : 정세훈
    지각 5회 이상 : 못 말리는 아가씨
    일찍 도착 10번 : 최원탁
    1만 포인트 : 만수르
     */

    public Long titleProvider(Long userId) {
        Users users = findUsersByUserId(userId);

        if (isBettingLoseGoe10(users)) {
            provideTitleToUser(users, 베팅_10회_패배);
        }
        if (isBettingWinGoe5(users)) {
            provideTitleToUser(users, 베팅_5회_승리);
        }
        if (isLateTimesGoe5(users)) {
            provideTitleToUser(users, 지각_5회_이상);
        }
        if (isEarlyArrivalTimesGoe10(users)) {
            provideTitleToUser(users, 일찍_도착_10번);
        }
        if (isPointGoe10000(users)) {
            provideTitleToUser(users, 일만_포인트_달성);
        }

        return users.getId();
    }

    private Users findUsersByUserId(Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_USER));
    }

    // 타이틀 부여하는 로직
    public Long provideTitleToUser(Users users, String titleName) {
        Title title = titleRepository.findByTitleName(titleName).orElseThrow(
                () -> new NoSuchEntityException(ErrorCode.NO_SUCH_TITLE)
        );
        UserTitle userTitle = UserTitle.builder()
                .user(users)
                .title(title)
                .build();

        userTitleRepository.save(userTitle);
        users.addTitle(userTitle);

        return userTitle.getId();
    }

    // 베팅 총 10회 패배 : 기부천사
    public boolean isBettingLoseGoe10(Users users) {
        // 이미 가지고 있기 때문에 추가X
        if (users.hasTitle(베팅_10회_패배))
            return false;

        Long userId = users.getId();

        // 베팅 걸어서 지거나 베팅 당했는데 상대가 이기거나
        // BettorId && ResultType == Lose || TargetId && ResultType == Win
        long countBettor = bettingRepository.countByBettorIdAndResultType(userId, ResultType.LOSE);
        long countTarget = bettingRepository.countByTargetUserIdAndResultType(userId, ResultType.WIN);

        long count = countBettor + countTarget;
        if (count == 10L) {
            return true;
        }
        return false;
    }

    // 베팅 5회 승리 : 정세훈
    public boolean isBettingWinGoe5(Users users) {
        // 이미 가지고 있기 때문에 추가X
        if (users.hasTitle(베팅_5회_승리))
            return false;

        Long userId = users.getId();

        // 베팅 걸어서 이기거나 베팅 당했는데 상대가 지거나
        // BettorId && ResultType == Lose || TargetId && ResultType == Win
        long countBettor = bettingRepository.countByBettorIdAndResultType(userId, ResultType.WIN);
        long countTarget = bettingRepository.countByTargetUserIdAndResultType(userId, ResultType.LOSE);

        long count = countBettor + countTarget;
        if (count == 5L) {
            return true;
        }
        return false;
    }

    // 지각 5회 이상 : 못 말리는 아가씨
    public boolean isLateTimesGoe5(Users users) {
        // 이미 가지고 있기 때문에 추가X
        if (users.hasTitle(베팅_5회_승리))
            return false;
        // UserArrivalData 에서 timeDifference가 양수면 지각
        List<UserArrivalData> userArrivalDatas = scheduleRepository.findUserArrivalDataByUserId(users.getId());
        int count = 0;
        for (UserArrivalData userArrivalData : userArrivalDatas) {
            if(userArrivalData.getTimeDifference() > 0) {
                count++;
            }
        }
        if (count == 5)
            return true;

        return false;
    }

    // 일찍 도착 10번 : 최원탁
    public boolean isEarlyArrivalTimesGoe10(Users users) {
        // 이미 가지고 있기 때문에 추가X
        if (users.hasTitle(베팅_5회_승리))
            return false;
        // UserArrivalData 에서 timeDifference가 음수면 일찍 도착
        List<UserArrivalData> userArrivalDatas = scheduleRepository.findUserArrivalDataByUserId(users.getId());
        int count = 0;
        for (UserArrivalData userArrivalData : userArrivalDatas) {
            if(userArrivalData.getTimeDifference() < 0) {
                count++;
            }
        }
        if (count == 10)
            return true;

        return false;
    }

    // 1만 포인트 : 만수르
    public boolean isPointGoe10000(Users users) {
        // 이미 가지고 있기 때문에 추가X
        if (users.hasTitle(베팅_5회_승리))
            return false;

        if (users.getPoint() >= 10000)
            return true;

        return false;
    }


}
