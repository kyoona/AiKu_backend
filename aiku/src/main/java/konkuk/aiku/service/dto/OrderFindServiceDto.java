package konkuk.aiku.service.dto;

import konkuk.aiku.domain.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@AllArgsConstructor
public class OrderFindServiceDto {

    private Long orderId;
    private ItemServiceDto item;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private int price;
    private int eventPrice;
    private String eventDescription;
    private int count;

}
