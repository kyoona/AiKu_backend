package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String userImg;
    private String phoneNumber;
    private int point;
    private TitleResponseDTO title;
}
