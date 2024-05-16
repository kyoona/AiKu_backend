package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class MessageTest {

    private String a;

    @Test
    @DisplayName("RealTimeLocationMessage 변환")
    void realTimeLocationMessage() {
        //given
        Long userId = 1l;
        String userName = "userA";
        String userImg = "url";
        Double latitude = 127.1;
        Double longitude = 127.1;

        Users user = createUser(userId, userName, userImg);

        //when
        Map<String, String> stringMap = RealTimeLocationMessage.createMessage(user, latitude, longitude)
                .toStringMap();

        //then
        assertThat(stringMap).containsEntry("userId", String.valueOf(userId));
        assertThat(stringMap).containsEntry("userName", userName);
        assertThat(stringMap).containsEntry("userImg", userImg);
        assertThat(stringMap).containsEntry("latitude", String.valueOf(latitude));
        assertThat(stringMap).containsEntry("longitude", String.valueOf(longitude));
    }

    @Test
    @DisplayName("sendingEmojiMessage 변환")
    public void sendingEmojiMessage() {
        //given
        Long senderId = 1l;
        String senderName = "userA";
        String senderImg = "url";
        Users sender = createUser(senderId, senderName, senderImg);

        Long receiverId = 2l;
        String receiverName = "userB";
        String receiverImg = "url2";
        Users receiver = createUser(receiverId, receiverName, receiverImg);

        Emoji emojiType = Emoji.HEART;

        //when
        Map<String, String> stringMap = SendingEmojiMessage.createMessage(sender, receiver, emojiType)
                .toStringMap();

        //then
        assertThat(stringMap).containsEntry("senderId", String.valueOf(senderId));
        assertThat(stringMap).containsEntry("senderName", senderName);
        assertThat(stringMap).containsEntry("senderImg", senderImg);
        assertThat(stringMap).containsEntry("receiverId", String.valueOf(receiverId));
        assertThat(stringMap).containsEntry("receiverName", receiverName);
        assertThat(stringMap).containsEntry("receiverImg", receiverImg);
        assertThat(stringMap).containsEntry("emojiType", String.valueOf(emojiType));
    }

    public Users createUser(Long userId, String userName, String userImg){
        return new Users(userId, userName, "01000000000", userImg, 1L, "pass",
                new Setting(true, true, true, true,true),
                null, null, 1000, UserRole.USER, "1", LocalDateTime.now(), "refresh");
    }
}