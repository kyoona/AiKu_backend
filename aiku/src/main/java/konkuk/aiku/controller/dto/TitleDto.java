package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Title;
import konkuk.aiku.service.dto.TitleServiceDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TitleDto {
    private String titleName;
    private String description;

    public TitleServiceDto toServiceDto() {
        return TitleServiceDto.builder()
                .titleName(titleName)
                .description(description)
                .build();
    }
}
