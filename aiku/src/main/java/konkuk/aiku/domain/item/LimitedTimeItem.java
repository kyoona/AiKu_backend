package konkuk.aiku.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("LimitedTime")
@Getter @Setter
public class LimitedTimeItem extends Item{
    private int validDays;
}
