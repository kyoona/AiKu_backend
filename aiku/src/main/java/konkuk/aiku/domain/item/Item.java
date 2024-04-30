package konkuk.aiku.domain.item;

import jakarta.persistence.*;
import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.TimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "itemType")
@Getter @Setter
public class Item extends TimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    private String itemName;
    private String itemImg;

    @Enumerated(value = EnumType.STRING)
    private ItemCategory itemCategory;
    private int price;
    private int eventPrice;
    private String eventDescription;

    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus;
}
