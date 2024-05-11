package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ItemDto {
    private String itemName;
    private ItemCategory itemCategory;
    private int price;
    private int eventPrice;
    private EventStatus eventStatus;

    public Item toEntity() {
        return Item.builder()
                .itemName(itemName)
                .itemCategory(itemCategory)
                .price(price)
                .eventPrice(eventPrice)
                .eventStatus(eventStatus)
                .build();

    }
}
