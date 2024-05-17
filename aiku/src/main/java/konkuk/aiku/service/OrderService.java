package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.domain.item.Item;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.ItemRepository;
import konkuk.aiku.repository.OrdersRepository;
import konkuk.aiku.service.dto.ItemServiceDto;
import konkuk.aiku.service.dto.OrderFindServiceDto;
import konkuk.aiku.service.dto.OrderServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrdersRepository ordersRepository;

    public List<Long> addOrders(Users users, List<OrderServiceDto> orderServiceDtos) {
        List<Long> orderIds = new ArrayList<>();

        for (OrderServiceDto orderServiceDto : orderServiceDtos) {
            ItemServiceDto itemDto = orderServiceDto.getItem();
            Item item = findItemById(itemDto.getId());
            Orders orders = orderServiceDto.toEntity();

            orders.setUserAndItem(users, item);
            orders.changeStatus(OrderStatus.SUCCESS);
            ordersRepository.save(orders);

            orderIds.add(orders.getId());
        }

        return orderIds;
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_ITEM));
    }

    public List<OrderFindServiceDto> findOrders(Users users, String startDate, String endDate, Integer minPrice, Integer maxPrice, String itemName, String itemType) {
        return ordersRepository.findOrdersInCondition(
                users.getId(), startDate, endDate,
                minPrice, maxPrice, itemName, itemType
        );
    }

    public Orders findOrderById(Long orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_ORDER));
    }
}
