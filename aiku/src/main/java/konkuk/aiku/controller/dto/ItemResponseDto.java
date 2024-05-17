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
@ToString
public class ItemResponseDto {
    private Long itemId;
    private String itemName;
    private ItemCategory itemCategory;
    private int price;
    private int eventPrice;
    private String itemImg;
    private EventStatus eventStatus;

    public static ItemResponseDto toDto(ItemServiceDto item) {
        return ItemResponseDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .itemImg(item.getItemImg())
                .itemCategory(item.getItemCategory())
                .price(item.getPrice())
                .eventPrice(item.getEventPrice())
                .eventStatus(item.getEventStatus())
                .build();
    }

    public static ItemResponseDto fromEntityToDto(Item item) {
        return ItemResponseDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .itemImg(item.getItemImg())
                .itemCategory(item.getItemCategory())
                .price(item.getPrice())
                .eventPrice(item.getEventPrice())
                .eventStatus(item.getEventStatus())
                .build();
    }
}
