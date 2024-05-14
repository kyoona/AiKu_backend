package konkuk.aiku.service.dto;

import jakarta.persistence.*;
import konkuk.aiku.domain.TimeEntity;
import konkuk.aiku.domain.Title;
import konkuk.aiku.domain.UserTitle;
import lombok.*;

@Getter @Builder
public class TitleServiceDto extends TimeEntity {

    private Long id;
    private String titleName;
    private String description;
    private String titleImg;

    public static TitleServiceDto toDto(UserTitle userTitle) {
        Title title = userTitle.getTitle();

        return TitleServiceDto.builder()
                .id(title.getId())
                .titleName(title.getTitleName())
                .description(title.getDescription())
                .titleImg(title.getTitleImg())
                .build();
    }
}
