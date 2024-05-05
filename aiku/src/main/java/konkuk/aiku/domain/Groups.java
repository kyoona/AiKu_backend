package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Groups extends TimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    private String groupName;
    private String groupImg;

    @OneToMany(mappedBy = "group")
    private List<Schedule> schedules;

    private String description;
}
