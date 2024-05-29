package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class RacingAcceptEvent {
    private final Long bettorId;
}
