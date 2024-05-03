package konkuk.aiku.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupServiceDTO {
    private Long id;
    private String groupName;
    private String groupImg;
    private String description;
}
