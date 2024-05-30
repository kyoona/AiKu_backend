package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RacingDenyEvent {
    private final Long scheduleId;
    private final Long bettingId;
}
