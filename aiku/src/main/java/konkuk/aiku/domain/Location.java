package konkuk.aiku.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {
    private Double latitude;
    private Double longitude;
    private String locationName;
}
