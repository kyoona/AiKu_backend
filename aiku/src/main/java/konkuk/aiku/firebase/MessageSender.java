package konkuk.aiku.firebase;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MessageSender {
    public void sendRealTimeLocation(Long userId, Double latitude, Double longitude, List<String> receiverTokens){
        MulticastMessage message = MulticastMessage.builder()
                .putData("userId", String.valueOf(userId))
                .putData("latitude", String.valueOf(latitude))
                .putData("longitude", String.valueOf(longitude))
                .addAllTokens(receiverTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);
    }
}
