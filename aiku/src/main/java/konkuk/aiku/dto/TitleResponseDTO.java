package konkuk.aiku.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TitleResponseDTO {
    private Long titleId;
    private String titleName;
    private String description;
    private String titleImg;
}
