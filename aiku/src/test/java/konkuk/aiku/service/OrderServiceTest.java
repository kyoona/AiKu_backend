package konkuk.aiku.service;

import konkuk.aiku.controller.dto.ItemDto;
import konkuk.aiku.controller.dto.OrderItemSimpleDto;
import konkuk.aiku.controller.dto.OrdersAddDto;
import konkuk.aiku.domain.*;
import konkuk.aiku.domain.item.Item;
import konkuk.aiku.repository.ItemRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.OrderFindServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    AdminService adminService;
    @Autowired
    OrderService orderService;

    Users userA1;
    @BeforeEach
    void setUp() {
        userA1 = Users.builder()
                .username("userA1")
                .phoneNumber("010-1111-1111")
                .userImg("http://userA1.img")
                .point(1000)
                .fcmToken("fcm")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userA1);
    }

    @Test
    void addOrders() {
        ItemDto item1 = ItemDto.builder()
                .itemName("Item1")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.NORMAL)
                .price(400)
                .eventPrice(400)
                .eventDescription("이벤트 중이 아니에요")
                .build();

        ItemDto item2 = ItemDto.builder()
                .itemName("Item2")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(400)
                .eventPrice(200)
                .eventDescription("이벤트 중이에요")
                .build();

        ItemDto item3 = ItemDto.builder()
                .itemName("Item3")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(400)
                .eventPrice(300)
                .eventDescription("이벤트 중이에요")
                .build();

        Long item1Id = adminService.addItem(item1.toServiceDto());
        Long item2Id = adminService.addItem(item2.toServiceDto());
        Long item3Id = adminService.addItem(item3.toServiceDto());

        OrderItemSimpleDto item1Order = OrderItemSimpleDto.builder()
                .itemId(item1Id)
                .count(3)
                .build();

        OrderItemSimpleDto item2Order = OrderItemSimpleDto.builder()
                .itemId(item2Id)
                .count(2)
                .build();

        OrderItemSimpleDto item3Order = OrderItemSimpleDto.builder()
                .itemId(item3Id)
                .count(1)
                .build();

        List<OrderItemSimpleDto> orderList = List.of(item1Order, item2Order, item3Order);

        OrdersAddDto ordersAddDto = OrdersAddDto.builder()
                .userId(userA1.getId())
                .items(orderList)
                .build();

        List<Long> orderIds = orderService.addOrders(userA1, ordersAddDto.toServiceDtos());

        for (Long orderId : orderIds) {
            Orders orders = orderService.findOrderById(orderId);
            log.info("order={}", orders);
        }

        Assertions.assertThat(orderIds.size()).isEqualTo(3);

    }

    @Test
    void findOrders() {
        ItemDto item1 = ItemDto.builder()
                .itemName("Item1")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.NORMAL)
                .price(400)
                .eventPrice(200)
                .eventDescription("이벤트 중이 아니에요")
                .build();

        ItemDto item2 = ItemDto.builder()
                .itemName("Item2")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(400)
                .eventPrice(300)
                .eventDescription("이벤트 중이에요")
                .build();

        ItemDto item3 = ItemDto.builder()
                .itemName("Item3")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(400)
                .eventPrice(400)
                .eventDescription("이벤트 중이에요")
                .build();

        Long item1Id = adminService.addItem(item1.toServiceDto());
        Long item2Id = adminService.addItem(item2.toServiceDto());
        Long item3Id = adminService.addItem(item3.toServiceDto());

        OrderItemSimpleDto item1Order = OrderItemSimpleDto.builder()
                .itemId(item1Id)
                .count(3)
                .build();

        OrderItemSimpleDto item2Order = OrderItemSimpleDto.builder()
                .itemId(item2Id)
                .count(2)
                .build();

        OrderItemSimpleDto item3Order = OrderItemSimpleDto.builder()
                .itemId(item3Id)
                .count(1)
                .build();

        List<OrderItemSimpleDto> orderList = List.of(item1Order, item2Order, item3Order);

        OrdersAddDto ordersAddDto = OrdersAddDto.builder()
                .userId(userA1.getId())
                .items(orderList)
                .build();

        orderService.addOrders(userA1, ordersAddDto.toServiceDtos());

        List<OrderFindServiceDto> orders = orderService.findOrders(userA1, null, null, 200, 350, null, null);
        for (OrderFindServiceDto order : orders) {
            log.info("order={}", order);
        }

    }
}