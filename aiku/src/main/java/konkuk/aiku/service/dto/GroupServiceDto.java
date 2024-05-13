package konkuk.aiku.service.dto;

import konkuk.aiku.controller.dto.GroupDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GroupServiceDto {
    private Long id;
    private String groupName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
