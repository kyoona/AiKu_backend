package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.OrderServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Builder
public class OrdersAddDto {
    private Long userId;
    private List<OrderItemSimpleDto> items;

    public List<OrderServiceDto> toServiceDtos() {
        return items.stream()
                .map(OrderItemSimpleDto::toServiceDto)
                .collect(Collectors.toList());
    }
}
