package ro.msg.learning.shop.dto;

import java.util.UUID;

public record OrderItem(UUID productId, int quantity) {}
