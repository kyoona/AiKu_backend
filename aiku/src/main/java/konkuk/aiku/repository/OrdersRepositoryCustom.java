package konkuk.aiku.repository;

import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.service.dto.OrderFindServiceDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepositoryCustom {
    List<OrderFindServiceDto> findOrdersInCondition(Long userId, LocalDateTime startDate, LocalDateTime endDate, Integer minPrice, Integer maxPrice, String itemName, ItemCategory itemCategory);
}
