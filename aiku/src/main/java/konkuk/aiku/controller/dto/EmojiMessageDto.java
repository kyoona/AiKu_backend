package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotNull;
import konkuk.aiku.domain.Emoji;
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
    private Emoji emojiType;
}
