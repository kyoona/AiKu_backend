package konkuk.aiku.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Default")
public class DefaultItem extends Item{
}
