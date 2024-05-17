package konkuk.aiku.domain.item;

import jakarta.persistence.*;
import konkuk.aiku.controller.dto.ItemDto;
import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.TimeEntity;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "itemType")
@NoArgsConstructor
@Getter @Setter @ToString
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

    @Builder
    public Item(String itemName, String itemImg, ItemCategory itemCategory, int price, int eventPrice, EventStatus eventStatus, String eventDescription) {
        this.itemName = itemName;
        this.itemImg = itemImg;
        this.itemCategory = itemCategory;
        this.price = price;
        this.eventPrice = eventPrice;
        this.eventStatus = eventStatus;
        this.eventDescription = eventDescription;
    }

    public void updateItem(String itemName, ItemCategory itemCategory, int price, int eventPrice, EventStatus eventStatus, String eventDescription) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.price = price;
        this.eventPrice = eventPrice;
        this.eventStatus = eventStatus;
        this.eventDescription = eventDescription;
    }
}
