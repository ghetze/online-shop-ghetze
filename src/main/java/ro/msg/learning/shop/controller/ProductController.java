package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAll() {
        return ProductMapper.toDtoList(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable UUID id) {
        return ProductMapper.toDto(productService.getProductById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody ProductDto dto) {
        Product product = ProductMapper.toEntity(dto);
        return ProductMapper.toDto(productService.createProduct(product, dto.getCategoryId()));
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable UUID id, @RequestBody ProductDto dto) {
        Product product = ProductMapper.toEntity(dto);
        return ProductMapper.toDto(productService.updateProduct(id, product, dto.getCategoryId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }
}
