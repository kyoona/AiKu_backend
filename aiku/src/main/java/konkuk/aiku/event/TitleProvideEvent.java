package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TitleProvideEvent {
    private Long userId;
    private Long titleId;
}
