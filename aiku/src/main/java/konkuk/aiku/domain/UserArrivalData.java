package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class UserArrivalData extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userArrivalDataId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
    private LocalDateTime arrivalTime;
    private int timeDifference;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "startLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "startLongitude")),
            @AttributeOverride(name = "locationName", column = @Column(name = "startLocationName"))
    })
    private Location startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "endLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "endLongitude")),
            @AttributeOverride(name = "locationName", column = @Column(name = "endLocationName"))
    })
    private Location endLocation;

    private UserArrivalData(Users user, Schedule schedule, LocalDateTime arrivalTime) {
        this.user = user;
        this.schedule = schedule;
        this.arrivalTime = arrivalTime;
    }

    //==생성 메서드==
    public static UserArrivalData createUserArrivalData(Users user, Schedule schedule, LocalDateTime arrivalTime){
        UserArrivalData userArrivalData = new UserArrivalData(user, schedule, arrivalTime);
        userArrivalData.setTimeDifference(schedule.getScheduleTime());
        return userArrivalData;
    }

    //==편의 메서드==
    private void setTimeDifference(LocalDateTime scheduleTime){
        timeDifference = (int) Duration.between(scheduleTime, arrivalTime).toMinutes();
    }
}
