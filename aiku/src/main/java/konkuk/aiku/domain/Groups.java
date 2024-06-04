package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Groups extends TimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupId")
    private Long id;

    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroup> userGroups = new ArrayList<>();
    private int userCount = 1; //그룹 생성한 사람 기본 참여

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    private String description;

    private Groups(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    //==생성 메서드==
    public static Groups createGroups(Users user, String groupName, String description){
        Groups group = new Groups(groupName, description);
        group.addUser(user);
        return group;
    }

    //==연관 관계 메서드==
    public void updateGroup(String groupName, String description){
        this.groupName = groupName;
        this.description = description;
    }

    public void addUser(Users user){
        UserGroup userGroup = new UserGroup(user, this);
        this.userGroups.add(userGroup);
    }

    public void deleteUser(UserGroup userGroup){
        userGroups.remove(userGroup);
    }

    public void addSchedule(Schedule schedule){
        schedule.setGroup(this);
        schedules.add(schedule);
    }

    public void deleteSchedule(Schedule schedule){
        schedules.remove(schedule);
    }

    public void clearSchedule(){
        schedules.clear();
    }
}
