package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import konkuk.aiku.service.dto.GroupServiceDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupDto {
    @NotBlank
    @Size(max = 15)
    private String groupName;

    @NotNull
    @Size(max = 20)
    private String description;

    public GroupServiceDto toServiceDto(){
        return GroupServiceDto.builder()
                .groupName(this.groupName)
                .description(this.description)
                .build();
    }
}
