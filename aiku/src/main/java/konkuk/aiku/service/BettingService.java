package konkuk.aiku.service;

import konkuk.aiku.controller.dto.BettingAddDto;
import konkuk.aiku.controller.dto.BettingModifyDto;
import konkuk.aiku.controller.dto.BettingResponseDto;
import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.BettingRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BettingService {

    private final BettingRepository bettingRepository;
    private final ScheduleRepository scheduleRepository;
    private final UsersRepository usersRepository;


    private Optional<UserSchedule> findUserInSchedule(Long userId, Long scheduleId) {
        return scheduleRepository.findUserScheduleByUserIdAndScheduleId(userId, scheduleId);
    }

    private Betting findBettingById(Long bettingId) {
        return bettingRepository.findById(bettingId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_BETTING));
    }

    private Users findUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_USERS));
    }

    public Long addBetting(Users users, Long scheduleId, BettingAddDto bettingAddDto) {
        Optional<UserSchedule> userInSchedule = findUserInSchedule(users.getId(), scheduleId);

        if (userInSchedule.isEmpty()) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        Users targetUser = usersRepository.findById(bettingAddDto.getTargetUserId())
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_USERS));

        UserSchedule userSchedule = userInSchedule.get();

        Betting betting = Betting.builder()
                .bettor(users)
                .targetUser(targetUser)
                .point(bettingAddDto.getPoint())
                .schedule(userSchedule.getSchedule())
                .bettingType(bettingAddDto.getBettingType())
                .build();

        bettingRepository.save(betting);

        return betting.getId();
    }

    public BettingResponseDto findBetting(Long bettingId) {
        Betting betting = findBettingById(bettingId);

        return BettingResponseDto.toDto(betting);
    }

    public Long updateBetting(Users users, Long scheduleId, Long bettingId, BettingModifyDto bettingModifyDto) {
        Optional<UserSchedule> userInSchedule = findUserInSchedule(users.getId(), scheduleId);

        if (userInSchedule.isEmpty()) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        // Schedule 검증
        Betting betting = findBettingById(bettingId);
        Users target = findUserById(bettingModifyDto.getTargetUserId());

        betting.setTargetUser(target);
        betting.setPoint(bettingModifyDto.getPoint());

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

    public List<BettingResponseDto> getBettingsByType(Long scheduleId, String bettingType) {
        List<Betting> bettings = bettingRepository.findBettingsByScheduleIdAndBettingType(scheduleId, BettingType.valueOf(bettingType));

        return bettings.stream()
                .map(BettingResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
