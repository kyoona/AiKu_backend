package konkuk.aiku.service.dto;

import konkuk.aiku.domain.OrderStatus;
import konkuk.aiku.domain.Orders;
import konkuk.aiku.domain.item.Item;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class OrderServiceDto {
    private ItemServiceDto item;
    private OrderStatus status;
    private int price;
    private int eventPrice;
    private String eventDescription;
    private int count;

    public Orders toEntity() {
        return Orders.builder()
                .item(item.toEntity())
                .price(item.getPrice())
                .eventPrice(item.getEventPrice())
                .eventDescription(item.getEventDescription())
                .count(count)
                .build();
    }

}
