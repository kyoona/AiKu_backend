package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationServiceDTO {
    private Double latitude;
    private Double longitude;
    private String locationName;

    public static LocationServiceDTO toDto(Location location){
        return new LocationServiceDTO(location.getLatitude(), location.getLongitude(), location.getLocationName());
    }
}
