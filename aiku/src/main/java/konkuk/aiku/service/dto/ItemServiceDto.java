package konkuk.aiku.service.dto;

import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.item.Item;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class ItemServiceDto {
    private Long id;

    private String itemName;
    private String itemImg;

    private ItemCategory itemCategory;
    private int price;
    private int eventPrice;
    private String eventDescription;

    private EventStatus eventStatus;

    public Item toEntity() {
        return Item.builder()
                .itemName(itemName)
                .itemImg(itemImg)
                .itemCategory(itemCategory)
                .price(price)
                .eventPrice(eventPrice)
                .eventDescription(eventDescription)
                .eventStatus(eventStatus)
                .build();
    }
}
