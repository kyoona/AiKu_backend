package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.item.Item;
import konkuk.aiku.service.dto.ItemServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Builder
public class ItemDto {
    private String itemName;
    private ItemCategory itemCategory;
    private int price;
    private int eventPrice;
    private String eventDescription;;
    private EventStatus eventStatus;
    private String itemImg;

    public ItemServiceDto toServiceDto() {
        return ItemServiceDto.builder()
                .itemName(itemName)
                .itemCategory(itemCategory)
                .price(price)
                .eventPrice(eventPrice)
                .eventStatus(eventStatus)
                .eventDescription(eventDescription)
                .itemImg(itemImg)
                .build();

    }
}
