package konkuk.aiku.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleDTO {
    @NotBlank
    @Size(max = 15)
    private String scheduleName;
    @Valid
    private LocationDTO location;
    @NotNull
    private LocalDateTime scheduleTime;
}
