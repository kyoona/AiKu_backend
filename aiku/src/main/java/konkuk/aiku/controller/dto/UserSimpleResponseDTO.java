package konkuk.aiku.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSimpleResponseDTO {
    private String userId;
    private String username;
    private String userImg;
}
