package konkuk.aiku.service;

import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.TokenException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.firebase.FcmToken;
import konkuk.aiku.firebase.FcmTokenProvider;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
    private UsersRepository usersRepository;
    private FcmTokenProvider fcmTokenProvider;

    @Transactional
    public void saveToken(Users user, FcmToken fcmToken){
        String token = fcmToken.getToken();
        if (user.getFcmToken() != null) {
            throw new TokenException(ErrorCode.DUPLICATE_FCM_TOKEN);
        }
        fcmTokenProvider.validateFcmToken(token);
        user.setFcmToken(token);
    }

    @Transactional
    public void updateToken(Users user, FcmToken fcmToken){
        String token = fcmToken.getToken();
        if (user.getFcmToken() == null) {
            throw new TokenException(ErrorCode.NO_GENERATED_TOKEN);
        }
        fcmTokenProvider.validateFcmToken(token);
        user.setFcmToken(token);
    }
}
