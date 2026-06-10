package ro.msg.learning.shop.service;

import ro.msg.learning.shop.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product createProduct(Product product, UUID categoryId);

    Product updateProduct(UUID id, Product updatedData, UUID categoryId);

    void deleteProduct(UUID id);

    Product getProductById(UUID id);

    List<Product> getAllProducts();
}
