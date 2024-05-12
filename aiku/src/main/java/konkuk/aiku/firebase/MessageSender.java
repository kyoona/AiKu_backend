package konkuk.aiku.firebase;

import com.google.firebase.messaging.*;
import konkuk.aiku.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static konkuk.aiku.firebase.MessageSender.MessageTitle.*;

@Component
@Slf4j
public class MessageSender {
    public void sendRealTimeLocation(Users user, Double latitude, Double longitude, List<String> receiverTokens) {
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", USER_SCHEDULE_ARRIVAL.getTitle())
                .putData("userId", String.valueOf(user.getId()))
                .putData("userName", user.getUsername())
                .putData("userImg", user.getUserImg())
                .putData("latitude", String.valueOf(latitude))
                .putData("longitude", String.valueOf(longitude))
                .addAllTokens(receiverTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);
    }

    public void sendUserArrival(Users user, List<String> receiverTokens){
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", USER_SCHEDULE_ARRIVAL.getTitle())
                .putData("userId", String.valueOf(user.getId()))
                .putData("userName", user.getUsername())
                .putData("userImg", user.getUserImg())
                .addAllTokens(receiverTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);
    }

    protected enum MessageTitle{
        USER_SCHEDULE_ARRIVAL("USER_SCHEDULE_ARRIVAL"),
        USER_SCHEDULE_LOCATION("USER_SCHEDULE_LOCATION");

        private String title;

        MessageTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
