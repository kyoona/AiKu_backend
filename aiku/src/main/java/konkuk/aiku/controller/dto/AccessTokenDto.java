package konkuk.aiku.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AccessTokenDto {
    private Long kakaoId;
    private String accessToken;
}
