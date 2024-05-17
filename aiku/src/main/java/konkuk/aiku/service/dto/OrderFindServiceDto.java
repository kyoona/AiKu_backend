package konkuk.aiku.service.dto;

import konkuk.aiku.domain.OrderStatus;
import konkuk.aiku.domain.item.Item;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
public class OrderFindServiceDto {

    private Long orderId;
    private ItemServiceDto item;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private int price;
    private int eventPrice;
    private String eventDescription;
    private int count;

    @Builder
    public OrderFindServiceDto(Long orderId, ItemServiceDto item, LocalDateTime orderDate, OrderStatus status, int price, int eventPrice, String eventDescription, int count) {
        this.orderId = orderId;
        this.item = item;
        this.orderDate = orderDate;
        this.status = status;
        this.price = price;
        this.eventPrice = eventPrice;
        this.eventDescription = eventDescription;
        this.count = count;
    }

    public OrderFindServiceDto(Long orderId, Item item, LocalDateTime orderDate, OrderStatus status, int price, int eventPrice, String eventDescription, int count) {
        this.orderId = orderId;
        this.item = ItemServiceDto.toServiceDto(item);
        this.orderDate = orderDate;
        this.status = status;
        this.price = price;
        this.eventPrice = eventPrice;
        this.eventDescription = eventDescription;
        this.count = count;
    }
}
