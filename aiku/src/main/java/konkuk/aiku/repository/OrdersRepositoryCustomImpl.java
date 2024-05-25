package konkuk.aiku.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.QOrders;
import konkuk.aiku.service.dto.OrderFindServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrdersRepositoryCustomImpl implements OrdersRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private QOrders qOrders = QOrders.orders;

    @Override
    public List<OrderFindServiceDto> findOrdersInCondition(Long userId, String startDate, String endDate, Integer minPrice, Integer maxPrice, String itemName, String itemCategory) {

        return jpaQueryFactory.select(Projections.constructor(OrderFindServiceDto.class,
                        qOrders.id, qOrders.item, qOrders.createdAt, qOrders.status, qOrders.price, qOrders.eventPrice, qOrders.eventDescription, qOrders.count
                ))
                .from(qOrders)
                .where(
                        qOrders.user.id.eq(userId),
                        dateAfter(startDate),
                        dateBefore(endDate),
                        priceGoe(minPrice),
                        priceLoe(maxPrice),
                        itemNameEq(itemName),
                        itemTypeEq(itemCategory)
                ).orderBy(qOrders.createdAt.desc())
                .fetch();
    }

    private BooleanExpression dateAfter(String startDate) {
        if (startDate == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMdd");
        return qOrders.createdAt.after(LocalDate.parse(startDate, df).atStartOfDay());
    }

    private BooleanExpression dateBefore(String endDate) {
        if (endDate == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMdd");
        return qOrders.createdAt.before(LocalDate.parse(endDate, df).atStartOfDay());
    }

    private BooleanExpression priceGoe(Integer minPrice) {
        if (minPrice == null) {
            return null;
        }
        return qOrders.eventPrice.goe(minPrice);
    }

    private BooleanExpression priceLoe(Integer maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return qOrders.eventPrice.loe(maxPrice);
    }

    private BooleanExpression itemNameEq(String itemName) {
        if (itemName == null) {
            return null;
        }
        return qOrders.item.itemName.eq(itemName);
    }

    private BooleanExpression itemTypeEq(String itemCategory) {
        if (itemCategory == null) {
            return null;
        }
        return qOrders.item.itemCategory.eq(ItemCategory.valueOf(itemCategory));
    }
}
