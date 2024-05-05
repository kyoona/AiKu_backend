package konkuk.aiku.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationServiceDTO {
    private Double latitude;
    private Double longitude;
    private String locationName;
}
