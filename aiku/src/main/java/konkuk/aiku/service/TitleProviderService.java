package konkuk.aiku.service;


import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.BettingRepository;
import org.springframework.stereotype.Service;

@Service
public class TitleProviderService {
    BettingRepository bettingRepository;

    /*
    베팅 총 10회 패배 : 기부천사
    베팅 5회 승리 : 정세훈
    지각 5회 이상 : 못 말리는 아가씨
    일찍 도착 10번 : 최원탁
    1만 포인트 : 만수르
     */

    // 베팅 총 10회 패배 : 기부천사
    public boolean isBettingLoseGoe10(Users users) {
        Long userId = users.getId();


        return true;
    }

    // 베팅 5회 승리 : 정세훈
    public boolean isBettingWinGoe5(Users users) {
        return true;
    }

    // 지각 5회 이상 : 못 말리는 아가씨
    public boolean isLateTimesGoe5(Users users) {
        return true;
    }

    // 일찍 도착 10번 : 최원탁
    public boolean isEarlyArrivalTimesGoe10(Users users) {
        return true;
    }

    // 1만 포인트 : 만수르
    public boolean isPointGoe10000(Users users) {
        return true;
    }


}
