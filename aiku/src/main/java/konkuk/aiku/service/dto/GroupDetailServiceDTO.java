package konkuk.aiku.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class GroupDetailServiceDTO {
    private Long groupId;
    private String groupName;
    private String groupImg;
    private String description;
    private List<UserSimpleServiceDTO> users = new ArrayList<>();
}
