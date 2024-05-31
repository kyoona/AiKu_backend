package konkuk.aiku.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoiceResponseDto {
    private String date;
    private String time;
    private String place;
}
