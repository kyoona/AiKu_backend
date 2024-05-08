package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import konkuk.aiku.service.dto.LocationServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDto {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotBlank
    private String locationName;

    public static LocationDto toDto(LocationServiceDto serviceDTO){
        return new LocationDto(serviceDTO.getLatitude(), serviceDTO.getLongitude(), serviceDTO.getLocationName());
    }
}
