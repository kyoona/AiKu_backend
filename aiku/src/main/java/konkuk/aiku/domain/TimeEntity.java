package konkuk.aiku.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class TimeEntity {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime endAt;
}
