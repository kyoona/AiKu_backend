package konkuk.aiku.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.TokenException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class FcmTokenProvider {
    public boolean validateFcmToken(String fcmToken, LocalDateTime tokenCreateAt){
        long tokenPeriod = ChronoUnit.DAYS.between(tokenCreateAt, LocalDateTime.now());
        if(tokenPeriod > 60){
            throw new TokenException(ErrorCode.EXPIRATION_TOKEN);
        }

        return true;
    }
}
