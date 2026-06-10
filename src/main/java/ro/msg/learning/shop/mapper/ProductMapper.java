package ro.msg.learning.shop.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.entity.Product;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .categoryDescription(product.getCategory().getDescription())
                .build();
    }

    public static Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setWeight(dto.getWeight());
        product.setImageUrl(dto.getImageUrl());
        return product;
    }

    public static List<ProductDto> toDtoList(List<Product> products) {
        return products.stream().map(ProductMapper::toDto).toList();
    }

    public static List<Product> toEntityList(List<ProductDto> dtos) {
        return dtos.stream().map(ProductMapper::toEntity).toList();
    }
}
