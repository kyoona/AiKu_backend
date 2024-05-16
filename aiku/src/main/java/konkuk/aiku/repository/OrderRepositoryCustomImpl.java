package konkuk.aiku.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.QOrders;
import konkuk.aiku.service.dto.OrderFindServiceDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrdersRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderFindServiceDto> findOrdersByCondition(Long userId, LocalDateTime startDate, LocalDateTime endDate, Integer minPrice, Integer maxPrice, String itemName, ItemCategory itemCategory) {
        QOrders qOrders = QOrders.orders;
        return jpaQueryFactory.select(Projections.constructor(OrderFindServiceDto.class,
                        qOrders.id, qOrders.item, qOrders.createdAt, qOrders.status, qOrders.price, qOrders.eventPrice, qOrders.eventDescription, qOrders.count
                ))
                .from(qOrders)
                .where(
                        qOrders.user.id.eq(userId)
                                .and(qOrders.createdAt.after(startDate))
                                .and(qOrders.createdAt.before(endDate))
                                .and(qOrders.eventPrice.goe(minPrice))
                                .and(qOrders.eventPrice.loe(maxPrice))
                                .and(qOrders.item.itemName.eq(itemName))
                                .and(qOrders.item.itemCategory.eq(itemCategory))
                ).orderBy(qOrders.createdAt.desc())
                .fetch();
    }
}
