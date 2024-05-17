package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Title;
import konkuk.aiku.domain.UserTitle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Builder
@ToString
public class TitleResponseDto {
    private Long titleId;
    private String titleName;
    private String description;
    private String titleImg;

    public static TitleResponseDto toDto(UserTitle userTitle) {
        Title title = userTitle.getTitle();

        return TitleResponseDto.builder()
                .titleId(title.getId())
                .titleName(title.getTitleName())
                .description(title.getDescription())
                .titleImg(title.getTitleImg())
                .build();
    }

    public static TitleResponseDto toTitleDto(Title title) {

        return TitleResponseDto.builder()
                .titleId(title.getId())
                .titleName(title.getTitleName())
                .description(title.getDescription())
                .titleImg(title.getTitleImg())
                .build();
    }
}
