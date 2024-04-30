package konkuk.aiku.domain.userItem;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "Consumable")
@Getter @Setter
public class UserConsumableItem extends UserItem{
    private int itemCount;
}
