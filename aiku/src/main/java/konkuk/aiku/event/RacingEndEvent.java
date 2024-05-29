package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class RacingEndEvent {
    private final Long bettorId;
    private final Long targetId;
}
