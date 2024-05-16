package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.OrderResponseDto;
import konkuk.aiku.controller.dto.OrdersAddDto;
import konkuk.aiku.controller.dto.OrdersListResponseDto;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.OrderService;
import konkuk.aiku.service.dto.OrderFindServiceDto;
import konkuk.aiku.service.dto.OrderServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<List<Long>> addOrders(@AuthenticationPrincipal UserAdaptor userAdaptor, OrdersAddDto ordersAddDto) {
        Users users = userAdaptor.getUsers();
        List<OrderServiceDto> orderServiceDtos = ordersAddDto.toServiceDtos();
        List<Long> orderIds = orderService.addOrders(users, orderServiceDtos);

        return new ResponseEntity(orderIds, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<OrdersListResponseDto> findOrders(
            @AuthenticationPrincipal UserAdaptor userAdaptor,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "startDate", required = false) String endDate,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "itemName", required = false) String itemName,
            @RequestParam(value = "itemType", required = false) String itemType
    ) {
        Users users = userAdaptor.getUsers();
        List<OrderFindServiceDto> orders = orderService.findOrders(users, startDate, endDate, minPrice, maxPrice, itemName, itemType);

        List<OrderResponseDto> orderResponseDtos = orders.stream().map(OrderResponseDto::toDto).collect(Collectors.toList());

        OrdersListResponseDto ordersListResponseDto = OrdersListResponseDto.builder()
                .userId(users.getId())
                .orders(orderResponseDtos)
                .build();

        return new ResponseEntity<>(ordersListResponseDto, HttpStatus.OK);
    }
}
