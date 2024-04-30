package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Betting extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(fetch = FetchType.LAZY)
    private BettingResult result;

    @Enumerated
    private BettingType bettingType;
}
