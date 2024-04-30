package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class UserArrivalData extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userArrivalDataId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    private User user;
    private Schedule schedule;
    private LocalDateTime arrivalTime;
    private LocalDateTime timeDifference;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "startLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "startLongitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "startLocationName"))
    })
    private Location startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "endLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "endLongitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "endLocationName"))
    })
    private Location endLocation;
}
