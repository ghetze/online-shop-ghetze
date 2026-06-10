package ro.msg.learning.shop.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderDetailResponseDto {

    private UUID productId;
    private String productName;
    private UUID shippedFromLocationId;
    private String shippedFromLocationName;
    private int quantity;
}
