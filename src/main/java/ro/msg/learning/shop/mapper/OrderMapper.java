package ro.msg.learning.shop.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.dto.AddressDto;
import ro.msg.learning.shop.dto.CreateOrderRequest;
import ro.msg.learning.shop.dto.OrderDetailResponseDto;
import ro.msg.learning.shop.dto.OrderItem;
import ro.msg.learning.shop.dto.OrderRequestDto;
import ro.msg.learning.shop.dto.OrderResponseDto;
import ro.msg.learning.shop.entity.Order;
import ro.msg.learning.shop.entity.embeddable.Address;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static CreateOrderRequest toCreateOrderRequest(OrderRequestDto dto) {
        Address address = new Address(
                dto.getDeliveryAddress().getCountry(),
                dto.getDeliveryAddress().getCity(),
                dto.getDeliveryAddress().getCounty(),
                dto.getDeliveryAddress().getStreetAddress());
        List<OrderItem> items = dto.getProducts().stream()
                .map(item -> new OrderItem(item.getProductId(), item.getQuantity()))
                .toList();
        return new CreateOrderRequest(dto.getUserId(), dto.getOrderTimestamp(), address, items);
    }

    public static OrderResponseDto toDto(Order order) {
        AddressDto addressDto = AddressDto.builder()
                .country(order.getAddress().getCountry())
                .city(order.getAddress().getCity())
                .county(order.getAddress().getCounty())
                .streetAddress(order.getAddress().getStreetAddress())
                .build();

        List<OrderDetailResponseDto> details = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponseDto.builder()
                        .productId(detail.getProduct().getId())
                        .productName(detail.getProduct().getName())
                        .shippedFromLocationId(detail.getShippedFrom().getId())
                        .shippedFromLocationName(detail.getShippedFrom().getName())
                        .quantity(detail.getQuantity())
                        .build())
                .toList();

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .createdAt(order.getCreatedAt())
                .address(addressDto)
                .orderDetails(details)
                .build();
    }
}
