package konkuk.aiku.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Consumable")
@Getter @Setter
public class ConsumableItem extends Item{
    private int maxLimit;
}
