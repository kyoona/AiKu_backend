package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RacingAcceptEvent {
    private final Long bettorId;
}
