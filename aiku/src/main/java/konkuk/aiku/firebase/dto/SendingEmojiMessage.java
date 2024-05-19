package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.EmojiType;
import konkuk.aiku.domain.UserImgData;
import konkuk.aiku.domain.UserImgData.ImgType;
import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public class SendingEmojiMessage extends Message{
    protected String title;
    protected Long scheduleId;

    protected Long senderId;
    protected String senderName;
    protected String senderImg;
    protected ImgType senderImgData;
    protected String senderColorCode;

    protected Long receiverId;
    protected String receiverName;
    protected String receiverImg;
    protected ImgType receiverImgData;
    protected String receiverColorCode;

    protected EmojiType emojiType;


    public static SendingEmojiMessage createMessage(Users sender, Long scheduleId, Users receiver, EmojiType emojiType){
        return SendingEmojiMessage.builder()
                .title(MessageTitle.EMOJI_TO_USER.getTitle())
                .scheduleId(scheduleId)
                .senderId(sender.getId())
                .senderName(sender.getUsername())
                .senderImg(sender.getUserImg())
                .receiverId(receiver.getId())
                .receiverName(receiver.getUsername())
                .receiverImg(receiver.getUserImg())
                .emojiType(emojiType)
                .build();
    }
}
