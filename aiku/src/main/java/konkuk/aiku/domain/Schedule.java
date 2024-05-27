package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
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

    @Enumerated(value = EnumType.STRING)
    private ScheduleStatus status;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<UserSchedule> users = new ArrayList<>();
    private int userCount = 1; //스케줄 생성한 사람

    @OneToMany(mappedBy = "schedule")
    private List<Betting> bettings = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<Betting> racings = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<UserArrivalData> userArrivalDatas = new ArrayList<>();

    @Builder
    public Schedule(Long id, String scheduleName, Location location, LocalDateTime scheduleTime, ScheduleStatus status) {
        this.id = id;
        this.scheduleName = scheduleName;
        this.location = location;
        this.scheduleTime = scheduleTime;
        this.status = status;
    }

    //==수정 메서드==
    public void updateSchedule(String scheduleName, Location location, LocalDateTime scheduleTime){
        this.scheduleName = scheduleName;
        this.location = location;
        this.scheduleTime = scheduleTime;
    }

    //==편의 메서드==
    public void addUser(Users user, UserSchedule userSchedule) {
        userSchedule.setSchedule(this);
        userSchedule.setUser(user);
        this.users.add(userSchedule);
    }

    public void deleteUser(Users user, UserSchedule userSchedule){
        users.remove(userSchedule);
        userSchedule.setSchedule(null);
        userSchedule.setUser(null);
    }

    public void addUserArrivalData(Users user, LocalDateTime arrivalTime){
        UserArrivalData userArrivalData = UserArrivalData.createUserArrivalData(user, this.group, this, arrivalTime);
        userArrivalDatas.add(userArrivalData);
    }

    //==Setter==
    protected void setGroup(Groups group) {
        this.group = group;
    }

    public void addBetting(Betting betting) {
        this.bettings.add(betting);
    }

    public void addRacing(Betting betting) {
        this.racings.add(betting);
    }
}
