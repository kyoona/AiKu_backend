package konkuk.aiku.firebase;

import com.google.firebase.messaging.*;
import konkuk.aiku.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MessageSender {
    public void sendRealTimeLocation(Users user, Double latitude, Double longitude, List<String> receiverTokens) {
        MulticastMessage message = MulticastMessage.builder()
                .putData("userId", String.valueOf(user.getId()))
                .putData("userName", user.getUsername())
                .putData("userImg", user.getUserImg())
                .putData("latitude", String.valueOf(latitude))
                .putData("longitude", String.valueOf(longitude))
                .addAllTokens(receiverTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);
    }
}
