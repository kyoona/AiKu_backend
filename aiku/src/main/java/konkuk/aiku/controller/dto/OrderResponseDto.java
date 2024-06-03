package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.OrderStatus;
import konkuk.aiku.domain.item.Item;
import konkuk.aiku.service.dto.ItemServiceDto;
import konkuk.aiku.service.dto.OrderFindServiceDto;
import konkuk.aiku.service.dto.OrderServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private ItemResponseDto item;
    private int count;

    public static OrderResponseDto toDto(OrderFindServiceDto order) {
        ItemServiceDto itemServiceDto = order.getItem();

        ItemResponseDto itemResponseDto = ItemResponseDto.toDto(itemServiceDto);

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getStatus())
                .item(itemResponseDto)
                .count(order.getCount())
                .build();
    }

    public static OrderResponseDto toDto(OrderServiceDto order) {
        ItemServiceDto itemServiceDto = order.getItem();

        ItemResponseDto itemResponseDto = ItemResponseDto.toDto(itemServiceDto);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .item(itemResponseDto)
                .count(order.getCount())
                .build();
    }
}
