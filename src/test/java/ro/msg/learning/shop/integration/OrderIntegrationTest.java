package ro.msg.learning.shop.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ro.msg.learning.shop.dto.CreateOrderRequest;
import ro.msg.learning.shop.dto.OrderItem;
import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Order;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.ProductCategory;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.entity.User;
import ro.msg.learning.shop.entity.embeddable.Address;
import ro.msg.learning.shop.exception.InsufficientStockException;
import ro.msg.learning.shop.service.OrderService;
import ro.msg.learning.shop.util.TestDataConfig;
import ro.msg.learning.shop.util.TestDataFactory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Import(TestDataConfig.class)
class OrderIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired OrderService orderService;
    @Autowired TestDataFactory factory;

    private User testUser;
    private Product testProduct;
    private Location testLocation;
    private ProductCategory testCategory;

    @BeforeEach
    void setUp() {
        testCategory = factory.saveCategory("Electronics");
        testProduct = factory.saveProduct("Laptop", BigDecimal.valueOf(999.99), 2.5, testCategory);
        testLocation = factory.saveLocation("Warehouse A");
        testUser = factory.saveUser("johndoe", "john@example.com");
        factory.saveStock(testProduct, testLocation, 10);
    }

    @AfterEach
    void tearDown() {
        factory.deleteAll();
    }

    @Test
    void createOrder_succeeds_withSufficientStock() {
        CreateOrderRequest request = new CreateOrderRequest(
                testUser.getId(),
                OffsetDateTime.now(),
                new Address("RO", "Bucharest", "Ilfov", "Str. Test 5"),
                List.of(new OrderItem(testProduct.getId(), 3))
        );

        Order order = orderService.createOrder(request);

        Order persisted = factory.findOrderWithDetails(order.getId());
        assertThat(persisted.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(persisted.getOrderDetails()).hasSize(1);
        assertThat(persisted.getOrderDetails().get(0).getQuantity()).isEqualTo(3);
        assertThat(persisted.getOrderDetails().get(0).getShippedFrom().getId()).isEqualTo(testLocation.getId());

        List<Stock> remainingStock = factory.findStocksForProducts(List.of(testProduct.getId()));
        assertThat(remainingStock).hasSize(1);
        assertThat(remainingStock.get(0).getQuantity()).isEqualTo(7);
    }

    @Test
    void createOrder_deductsStockToZero_andDeletesStockRow() {
        CreateOrderRequest request = new CreateOrderRequest(
                testUser.getId(),
                OffsetDateTime.now(),
                new Address("RO", "Bucharest", "Ilfov", "Str. Test 5"),
                List.of(new OrderItem(testProduct.getId(), 10))
        );

        orderService.createOrder(request);

        List<Stock> remainingStock = factory.findStocksForProducts(List.of(testProduct.getId()));
        assertThat(remainingStock).isEmpty();
    }

    @Test
    void createOrder_fails_whenRequestedQuantityExceedsStock() {
        CreateOrderRequest request = new CreateOrderRequest(
                testUser.getId(),
                OffsetDateTime.now(),
                new Address("RO", "Bucharest", "Ilfov", "Str. Test 5"),
                List.of(new OrderItem(testProduct.getId(), 99))
        );

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void createOrder_fails_whenProductHasNoStock() {
        Product noStockProduct = factory.saveProduct("Rare Item", BigDecimal.TEN, 0.5, testCategory);

        CreateOrderRequest request = new CreateOrderRequest(
                testUser.getId(),
                OffsetDateTime.now(),
                new Address("RO", "Bucharest", "Ilfov", "Str. Test 5"),
                List.of(new OrderItem(noStockProduct.getId(), 1))
        );

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(InsufficientStockException.class);
    }
}

