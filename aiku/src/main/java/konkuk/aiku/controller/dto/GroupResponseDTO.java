package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GroupResponseDTO {
    private Long groupId;
    private String groupName;
    private String description;
    private List<UserSimpleResponseDTO> users;
}
