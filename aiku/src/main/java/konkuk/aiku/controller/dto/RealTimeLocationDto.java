package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class RealTimeLocationDto {
    @Setter
    private Long id;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    public RealTimeLocationDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
