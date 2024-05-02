package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupDTO {
    @NotBlank
    @Size(max = 15)
    private String groupName;

    @NotNull
    @Size(max = 20)
    private String description;
}
