package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Groups group;
    private String scheduleName;

    @Embedded
    private Location location;
    private LocalDateTime scheduleTime;

    @Enumerated
    private ScheduleStatus status;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> users = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<Betting> bettings = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<Betting> racings = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<UserArrivalData> userArrivalDatas = new ArrayList<>();

    @Builder
    public Schedule(String scheduleName, Location location, LocalDateTime scheduleTime, ScheduleStatus status) {
        this.scheduleName = scheduleName;
        this.location = location;
        this.scheduleTime = scheduleTime;
        this.status = status;
    }

    public void addUser(Users user, UserSchedule userSchedule) {
        userSchedule.setSchedule(this);
        userSchedule.setUser(user);
        this.users.add(userSchedule);
    }

    public void setGroup(Groups group){
        this.group = group;
        group.addSchedule(this);
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }
}
