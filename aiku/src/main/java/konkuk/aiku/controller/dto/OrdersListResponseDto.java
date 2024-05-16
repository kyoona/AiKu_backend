package konkuk.aiku.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class OrdersListResponseDto {
    private Long userId;
    private List<OrderResponseDto> orders;
}
