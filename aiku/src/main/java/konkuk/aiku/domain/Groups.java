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
    private String groupImg;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    private String description;

    @Builder
    public Groups(String groupName, String groupImg, String description) {
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.description = description;
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }
}
