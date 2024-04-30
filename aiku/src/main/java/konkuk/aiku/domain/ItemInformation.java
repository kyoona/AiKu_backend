package konkuk.aiku.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ItemInformation {
    private int x;
    private int y;
    private int width;
    private int height;
}
