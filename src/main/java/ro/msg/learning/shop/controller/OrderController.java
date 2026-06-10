package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.CreateOrderRequest;
import ro.msg.learning.shop.dto.OrderRequestDto;
import ro.msg.learning.shop.dto.OrderResponseDto;
import ro.msg.learning.shop.entity.Order;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto dto) {
        CreateOrderRequest request = OrderMapper.toCreateOrderRequest(dto);
        Order order = orderService.createOrder(request);
        return OrderMapper.toDto(order);
    }
}
