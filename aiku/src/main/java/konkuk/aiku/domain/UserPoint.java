package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userPointId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;
    private int point;

    @Enumerated(EnumType.STRING)
    private PointChangeType pointChangeType;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Builder
    public UserPoint(Users user, int point, PointChangeType pointChangeType, PointType pointType) {
        this.user = user;
        this.point = point;
        this.pointChangeType = pointChangeType;
        this.pointType = pointType;
    }
}
