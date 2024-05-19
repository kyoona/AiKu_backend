package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserArrivalDto {
    @NotNull
    private LocalDateTime arrivalTime;
}
