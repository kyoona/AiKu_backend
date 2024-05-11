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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    private String description;

    @Builder
    public Groups(String groupName, String description) {
        this.groupName = groupName;
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

}
