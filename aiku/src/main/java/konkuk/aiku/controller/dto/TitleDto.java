package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Title;
import konkuk.aiku.service.dto.TitleServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TitleDto {
    private String titleName;
    private String description;
    private String titleImg;

    public TitleServiceDto toServiceDto() {
        return TitleServiceDto.builder()
                .titleName(titleName)
                .description(description)
                .titleImg(titleImg)
                .build();
    }

    @Builder
    public TitleDto(String titleName, String description, String titleImg) {
        this.titleName = titleName;
        this.description = description;
        this.titleImg = titleImg;
    }
}
