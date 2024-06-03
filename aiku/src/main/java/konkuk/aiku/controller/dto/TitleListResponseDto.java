package konkuk.aiku.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor @Getter
public class TitleListResponseDto {
    List<TitleResponseDto> data;
}
