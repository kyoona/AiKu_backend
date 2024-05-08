package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import konkuk.aiku.service.dto.LocationServiceDTO;
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

    public static LocationDTO toDto(LocationServiceDTO serviceDTO){
        return new LocationDTO(serviceDTO.getLatitude(), serviceDTO.getLongitude(), serviceDTO.getLocationName());
    }
}
