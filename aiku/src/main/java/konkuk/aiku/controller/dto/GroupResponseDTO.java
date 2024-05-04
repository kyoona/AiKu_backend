package konkuk.aiku.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupResponseDTO {
    private Long groupId;
    private String groupName;
    private String description;
    @Builder.Default private List<UserSimpleResponseDTO> users = new ArrayList<>();
}
