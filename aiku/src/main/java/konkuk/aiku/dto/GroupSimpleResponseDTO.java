package konkuk.aiku.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class GroupSimpleResponseDTO {
    private Long groupId;
    private String groupName;
    private int memberSize;
    private LocalDateTime lastScheduleTime;
}
