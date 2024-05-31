package konkuk.aiku.service.dto;

import konkuk.aiku.controller.dto.VoiceResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoiceApiResponseDto {
    private String DT; // 날짜
    private String TI; // 시간
    private String OG; // 장소
    private String status;

    public VoiceResponseDto toResponseDto() {
        return VoiceResponseDto.builder()
                .date(DT)
                .time(TI)
                .place(OG)
                .build();
    }
}
