package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RacingApplyEvent {
    private final Long targetId;
}
