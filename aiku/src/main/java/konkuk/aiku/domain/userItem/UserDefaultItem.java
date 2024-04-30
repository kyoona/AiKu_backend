package konkuk.aiku.domain.userItem;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import konkuk.aiku.domain.ItemInformation;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "Default")
@Getter @Setter
public class UserDefaultItem extends UserItem{
    private Boolean isUsed;

    @Embedded
    private ItemInformation itemInformation;
}
