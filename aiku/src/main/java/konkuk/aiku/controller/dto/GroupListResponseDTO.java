package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GroupListResponseDTO {
    private Long userId;
    private int groupsSize;
    private int pageNum;
    private int dataSize;
    private List<GroupSimpleResponseDTO> data;
}
