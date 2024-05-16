package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.Emoji;
import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;

public class SendingEmojiMessage extends Message{
    protected String title;

    protected Long senderId;
    protected String senderName;
    protected String senderImg;

    protected Long receiverId;
    protected String receiverName;
    protected String receiverImg;

    protected Emoji emojiType;

    @Builder(access = AccessLevel.PROTECTED)
    protected SendingEmojiMessage(Long senderId, String senderName, String senderImg, Long receiverId, String receiverName, String receiverImg, Emoji emojiType) {
        this.title = MessageTitle.EMOJI_TO_USER.getTitle();
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImg = senderImg;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverImg = receiverImg;
        this.emojiType = emojiType;
    }

    public static SendingEmojiMessage createMessage(Users sender, Users receiver, Emoji emojiType){
        return SendingEmojiMessage.builder()
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
