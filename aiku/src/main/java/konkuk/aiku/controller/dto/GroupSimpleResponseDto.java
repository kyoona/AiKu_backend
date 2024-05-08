package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class GroupSimpleResponseDto {
    private Long groupId;
    private String groupName;
    private int memberSize;
    private LocalDateTime lastScheduleTime;
}
