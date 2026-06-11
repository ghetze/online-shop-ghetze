package ro.msg.learning.shop.util;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.repository.UserRepository;

@TestConfiguration
public class TestDataConfig {

    @Bean
    public TestDataFactory testDataFactory(
            ProductCategoryRepository categoryRepository,
            ProductRepository productRepository,
            LocationRepository locationRepository,
            UserRepository userRepository,
            StockRepository stockRepository,
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        return new TestDataFactory(categoryRepository, productRepository, locationRepository,
                userRepository, stockRepository, orderRepository, orderDetailRepository);
    }
}
