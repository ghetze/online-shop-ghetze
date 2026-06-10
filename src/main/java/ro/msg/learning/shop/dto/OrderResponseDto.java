package ro.msg.learning.shop.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderResponseDto {

    private UUID id;
    private UUID userId;
    private OffsetDateTime createdAt;
    private AddressDto address;
    private List<OrderDetailResponseDto> orderDetails;
}
