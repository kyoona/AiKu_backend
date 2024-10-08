package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        Double latitude = 127.1;
        Double longitude = 127.1;

        Users user = createUser(userId, userName);

        //when
        Map<String, String> stringMap = RealTimeLocationMessage.createMessage(user, 1l, latitude, longitude)
                .toStringMap();

        //then
        assertThat(stringMap).containsEntry("userId", String.valueOf(userId));
        assertThat(stringMap).containsEntry("userName", userName);
        assertThat(stringMap).containsEntry("latitude", String.valueOf(latitude));
        assertThat(stringMap).containsEntry("longitude", String.valueOf(longitude));
    }

/*    @Test
    @DisplayName("sendingEmojiMessage 변환")
    public void sendingEmojiMessage() {
        //given
        Long senderId = 1l;
        String senderName = "userA";
        Users sender = createUser(senderId, senderName);

        Long receiverId = 2l;
        String receiverName = "userB";
        Users receiver = createUser(receiverId, receiverName);

        EmojiType emojiType = EmojiType.HEART;

        //when
        Map<String, String> stringMap = SendingEmojiMessage.createMessage(sender, 1l, receiver, emojiType)
                .toStringMap();

        //then
        assertThat(stringMap).containsEntry("senderId", String.valueOf(senderId));
        assertThat(stringMap).containsEntry("senderName", senderName);
        assertThat(stringMap).containsEntry("receiverId", String.valueOf(receiverId));
        assertThat(stringMap).containsEntry("receiverName", receiverName);
        assertThat(stringMap).containsEntry("emojiType", String.valueOf(emojiType));
    }*/

    public Users createUser(Long userId, String userName){
        return Users.builder()
                .id(userId)
                .username(userName)
                .setting(new Setting(true, true, true, true, true))
                .userImgData(new UserImgData(UserImgData.ImgType.DEFAULT1, "#000000"))
                .build();
    }
}