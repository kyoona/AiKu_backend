package konkuk.aiku.service;

import konkuk.aiku.domain.PointChangeType;
import konkuk.aiku.domain.PointType;
import konkuk.aiku.domain.UserPoint;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.UserPointRepository;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPointService {
    private final UsersRepository usersRepository;
    private final UserPointRepository userPointRepository;

    public Long addUserPoint(Long userId, int point, PointChangeType pointChangeType, PointType pointType) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_USER));

        if (pointChangeType.equals(PointChangeType.PLUS)) {
            // 포인트 추가된 경우
            users.plusPoint(point);
        }
        else {
            // 포인트 삭감된 경우
            users.minusPoint(point);
        }

        UserPoint userPoint = UserPoint.builder()
                .user(users)
                .point(point)
                .pointChangeType(pointChangeType)
                .pointType(pointType)
                .build();

        UserPoint save = userPointRepository.save(userPoint);

        return save.getId();
    }
}
