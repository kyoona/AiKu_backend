package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Schedule extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Group group;
    private String scheduleName;

    @Embedded
    private Location location;
    private LocalDateTime scheduleTime;

    @Enumerated
    private ScheduleStatus status;

    @OneToMany(mappedBy = "schedule")
    private List<UserSchedule> users;

    @OneToMany(mappedBy = "schedule")
    private List<Betting> bettings;

    @OneToMany(mappedBy = "schedule")
    private List<Betting> racings;

    @OneToMany(mappedBy = "schedule")
    private List<UserArrivalData> userArrivalDatas;
}
