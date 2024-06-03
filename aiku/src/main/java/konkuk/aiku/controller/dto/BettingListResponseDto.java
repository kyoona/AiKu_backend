package konkuk.aiku.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BettingListResponseDto {
    List<BettingResponseDto> data;
}
