package ro.msg.learning.shop.dto;

import ro.msg.learning.shop.entity.embeddable.Address;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID userId,
        OffsetDateTime orderTimestamp,
        Address deliveryAddress,
        List<OrderItem> products) {}
