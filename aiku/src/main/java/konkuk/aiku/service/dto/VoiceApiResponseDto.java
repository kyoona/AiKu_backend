package konkuk.aiku.service.dto;

import konkuk.aiku.controller.dto.VoiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class VoiceApiResponseDto {
    private String DT; // 날짜
    private String TI; // 시간
    private String OG; // 장소
    private String STATUS;

    public VoiceResponseDto toResponseDto() {
        return VoiceResponseDto.builder()
                .date(DT)
                .time(TI)
                .place(OG)
                .build();
    }
}
