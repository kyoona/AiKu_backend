package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotNull;
import konkuk.aiku.domain.EmojiType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmojiMessageDto {
    @NotNull
    private Long receiverId;
    @NotNull
    private EmojiType emojiType;
    private Double senderLatitude;
    private Double senderLongitude;
    private Double receiverLatitude;
    private Double receiverLongitude;
}
