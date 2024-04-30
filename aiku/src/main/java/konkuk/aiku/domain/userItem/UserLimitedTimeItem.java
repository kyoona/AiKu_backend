package konkuk.aiku.domain.userItem;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import konkuk.aiku.domain.ItemInformation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = "LimitedTime")
@Getter @Setter
public class UserLimitedTimeItem extends UserItem{
    private Boolean isUsed;

    @Embedded
    private ItemInformation itemInformation;
    private LocalDateTime validEndTime;
}
