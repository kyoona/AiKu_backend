package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Betting extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bettingId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bettorId")
    private Users bettor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetUserId")
    private Users targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    private int point;

    @Enumerated(value = EnumType.STRING)
    private ResultType resultType; // WIN, LOSE

    @Enumerated
    private BettingType bettingType; // RACING, BETTING

    @Enumerated
    private BettingStatus bettingStatus;

    public void updateBettingResult(ResultType resultType) {
        this.resultType = resultType;
    }

    @Builder
    public Betting(Users bettor, Users targetUser, Schedule schedule, int point, BettingType bettingType, BettingStatus bettingStatus) {
        this.bettor = bettor;
        this.targetUser = targetUser;
        this.schedule = schedule;
        this.point = point;
        this.bettingType = bettingType;
        this.bettingStatus = bettingStatus;
    }
}
