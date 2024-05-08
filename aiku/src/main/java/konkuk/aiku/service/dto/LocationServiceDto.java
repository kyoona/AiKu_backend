package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationServiceDto {
    private Double latitude;
    private Double longitude;
    private String locationName;

    public static LocationServiceDto toDto(Location location){
        return new LocationServiceDto(location.getLatitude(), location.getLongitude(), location.getLocationName());
    }
}
