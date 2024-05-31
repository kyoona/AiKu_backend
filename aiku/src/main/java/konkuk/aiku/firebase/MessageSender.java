package konkuk.aiku.firebase;

import com.google.firebase.messaging.*;
import konkuk.aiku.domain.UserImgData;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.MessagingException;
import konkuk.aiku.firebase.dto.MessageTitle;
import konkuk.aiku.firebase.dto.RealTimeLocationMessage;
import konkuk.aiku.firebase.dto.ScheduleMessage;
import konkuk.aiku.firebase.dto.UserArrivalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    public void sendMessageToUsers(Map<String, String> messageDataMap, List<String> receiverTokens) {
        log.info("Firebase sendMessageToUsers");
        MulticastMessage message = MulticastMessage.builder()
                .putAllData(messageDataMap)
                .addAllTokens(receiverTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);
    }

    public void sendMessageToUser(Map<String, String> messageDataMap, String receiverToken) {
        log.info("Firebase sendMessageToUser");
        Message message = Message.builder()
                .putAllData(messageDataMap)
                .setToken(receiverToken)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new MessagingException(ErrorCode.FAIL_TO_SEND_MESSAGE);
        }
    }

/*    public void testSend() {
        String token = "d0z74H_JTt2FkdmnFv_Rau:APA91bG4Q197dWCa9wd4790ivh6s4whhj8Ier5dQ0BU_g-a7fUAyJgR-bM-lTJZRGo78fkBT4JyG5-D-2-1JSwclB_LvdDjOjhWbH626m3sjfWTvMBGMlrUNG9uSjTLbRwfGko65kYK0";

        Map<String, String> stringMap = RealTimeLocationMessage.builder()
                .title(MessageTitle.USER_REAL_TIME_LOCATION.getTitle())
                .scheduleId(5L)
                .userId(4L)
                .userName("정세훈")
                .userImg("url")
                .imgData(UserImgData.ImgType.DEFAULT1)
                .colorCode("#000000")
//                .latitude(37.549186395087)
//                .longitude(127.07505567644)
                .latitude(37.549186395087)
                .longitude(127.07505567644)
                //건국
//                .latitude(37.544947955055)
//                .longitude(127.08198172427)
                .build()
                .toStringMap();

*//*        Map<String, String> stringMap2 = UserArrivalMessage.builder()
                .title(MessageTitle.USER_SCHEDULE_ARRIVAL.getTitle())
                .scheduleId(1L)
                .scheduleName("모각코")
                .userId(1L)
                .userName("임시 유저")
                .userImg("url")
                .imgData(UserImgData.ImgType.DEFAULT1)
                .colorCode("#000000")
                .build()
                .toStringMap();*//*
//        Map<String, String> stringMap = new ScheduleMessage(MessageTitle.SCHEDULE_MAP_OPEN, 5l, "모각코", "케이큐브", LocalDateTime.now()).toStringMap();

        Message message = Message.builder()
                .putAllData(stringMap)
//                .putData("title", "test")
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Message sent successfully: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            throw new MessagingException(ErrorCode.FAIL_TO_SEND_MESSAGE);
        }
    }*/
}
