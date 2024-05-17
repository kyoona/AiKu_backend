package konkuk.aiku.repository;

import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.service.dto.OrderFindServiceDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepositoryCustom {
    List<OrderFindServiceDto> findOrdersInCondition(Long userId, String startDate, String endDate, Integer minPrice, Integer maxPrice, String itemName, String itemCategory);
}
