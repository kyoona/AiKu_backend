package konkuk.aiku.service;

import konkuk.aiku.domain.PointChangeType;
import konkuk.aiku.domain.PointType;
import konkuk.aiku.domain.UserPoint;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository userPointRepository;

    public Long addUserPoint(Users users, int point, PointChangeType pointChangeType, PointType pointType) {
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
