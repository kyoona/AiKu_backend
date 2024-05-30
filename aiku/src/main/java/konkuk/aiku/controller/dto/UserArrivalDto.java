package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserArrivalDto {
    @NotNull
    private LocalDateTime arrivalTime;

    public UserArrivalDto(String arrivalTime) {
        this.arrivalTime = LocalDateTime.parse(arrivalTime);
    }
}
