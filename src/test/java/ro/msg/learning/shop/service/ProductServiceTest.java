package ro.msg.learning.shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.ProductCategory;
import ro.msg.learning.shop.exception.ResourceNotFoundException;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.util.TestEntityBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    ProductServiceImpl productService;

    private final UUID productId = UUID.randomUUID();
    private final UUID categoryId = UUID.randomUUID();

    @Test
    void getProductById_returnsProduct() {
        Product product = TestEntityBuilder.buildProduct(productId);
        when(productRepository.findByIdWithCategory(productId)).thenReturn(Optional.of(product));

        assertThat(productService.getProductById(productId)).isEqualTo(product);
    }

    @Test
    void getProductById_throwsWhenNotFound() {
        when(productRepository.findByIdWithCategory(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllProducts_returnsList() {
        List<Product> products = List.of(TestEntityBuilder.buildProduct(productId));
        when(productRepository.findAllWithCategory()).thenReturn(products);

        assertThat(productService.getAllProducts()).isEqualTo(products);
    }

    @Test
    void createProduct_savesAndReturns() {
        ProductCategory category = TestEntityBuilder.buildCategory(categoryId);
        Product product = new Product();
        when(productCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product, categoryId);

        assertThat(result.getCategory()).isEqualTo(category);
        verify(productRepository).save(product);
    }

    @Test
    void createProduct_throwsWhenCategoryNotFound() {
        when(productCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(new Product(), categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateProduct_updatesFields() {
        Product existing = TestEntityBuilder.buildProduct(productId);
        Product update = TestEntityBuilder.buildProduct(UUID.randomUUID(), "Updated", BigDecimal.TEN, 3.5, "http://img");
        when(productRepository.findByIdWithCategory(productId)).thenReturn(Optional.of(existing));

        Product result = productService.updateProduct(productId, update, null);

        assertThat(result.getName()).isEqualTo("Updated");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(result.getWeight()).isEqualTo(3.5);
        assertThat(result.getImageUrl()).isEqualTo("http://img");
    }

    @Test
    void updateProduct_updatesCategory_whenCategoryIdProvided() {
        ProductCategory newCategory = TestEntityBuilder.buildCategory(categoryId);
        Product existing = TestEntityBuilder.buildProduct(productId, "Original", BigDecimal.ONE, 1.0, "http://old");
        Product update = TestEntityBuilder.buildProduct(UUID.randomUUID(), "Updated", BigDecimal.TEN, 3.5, "http://new");
        when(productRepository.findByIdWithCategory(productId)).thenReturn(Optional.of(existing));
        when(productCategoryRepository.findById(categoryId)).thenReturn(Optional.of(newCategory));

        Product result = productService.updateProduct(productId, update, categoryId);

        assertThat(result.getCategory()).isEqualTo(newCategory);
        assertThat(result.getName()).isEqualTo("Updated");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void updateProduct_throwsWhenCategoryNotFound() {
        Product existing = TestEntityBuilder.buildProduct(productId);
        when(productRepository.findByIdWithCategory(productId)).thenReturn(Optional.of(existing));
        when(productCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(productId, new Product(), categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateProduct_throwsWhenNotFound() {
        when(productRepository.findByIdWithCategory(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(productId, new Product(), null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteProduct_deletesById() {
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProduct_throwsWhenNotFound() {
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
