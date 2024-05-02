package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSimpleResponseDTO {
    private Long userId;
    private String username;
    private String userImg;
}
