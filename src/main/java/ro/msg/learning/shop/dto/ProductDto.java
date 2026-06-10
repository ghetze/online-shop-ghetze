package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private double weight;
    private String imageUrl;
    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
}
