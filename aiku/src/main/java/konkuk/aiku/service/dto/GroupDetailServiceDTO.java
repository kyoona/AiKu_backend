package konkuk.aiku.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupDetailServiceDTO {
    private Long groupId;
    private String groupName;
    private String groupImg;
    private String description;
    @Builder.Default private List<UserSimpleServiceDTO> users = new ArrayList<>();
}
