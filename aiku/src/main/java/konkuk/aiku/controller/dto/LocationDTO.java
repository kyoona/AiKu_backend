package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDTO {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotBlank
    private String locationName;
}
