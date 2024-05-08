package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GroupListResponseDto {
    private Long userId;
    private int groupsSize;
    private int pageNum;
    private int dataSize;
    private List<GroupSimpleResponseDto> data;
}
