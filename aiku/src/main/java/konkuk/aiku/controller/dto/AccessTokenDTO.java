package konkuk.aiku.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AccessTokenDTO {
    private String kakaoId;
    private String accessToken;
}
