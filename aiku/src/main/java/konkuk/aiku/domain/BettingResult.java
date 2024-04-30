package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;

@Entity
public class BettingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bettingResultId")
    @Setter(value = AccessLevel.NONE)
    private Long id;
}
