package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.ItemServiceDto;
import konkuk.aiku.service.dto.OrderServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder
public class OrderItemSimpleDto {
    private Long itemId;
    private int count;

    public OrderServiceDto toServiceDto() {
        ItemServiceDto itemService = ItemServiceDto.builder().id(itemId).build();
        return OrderServiceDto.builder()
                .item(itemService)
                .count(count)
                .build();
    }
}
