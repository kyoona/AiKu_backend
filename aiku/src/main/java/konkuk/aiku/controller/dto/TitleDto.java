package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Title;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TitleDto {
    private String titleName;
    private String description;

    public Title toEntity() {
        return Title.builder()
                .titleName(titleName)
                .description(description)
                .build();
    }
}
