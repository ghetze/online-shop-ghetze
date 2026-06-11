package ro.msg.learning.shop.util;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Order;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.ProductCategory;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.entity.User;
import ro.msg.learning.shop.entity.embeddable.Address;
import ro.msg.learning.shop.enums.UserRole;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TestDataFactory {

    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductCategory saveCategory(String name) {
        ProductCategory c = new ProductCategory();
        c.setName(name);
        return categoryRepository.save(c);
    }

    public Product saveProduct(String name, BigDecimal price, double weight, ProductCategory category) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setWeight(weight);
        p.setCategory(category);
        return productRepository.save(p);
    }

    public Location saveLocation(String name) {
        Location l = new Location();
        l.setName(name);
        l.setAddress(new Address("RO", "Cluj", "Cluj", "Str. Main 1"));
        return locationRepository.save(l);
    }

    public User saveUser(String username, String email) {
        User u = new User();
        u.setFirstName("Test");
        u.setLastName("User");
        u.setUsername(username);
        u.setPassword("secret");
        u.setEmailAddress(email);
        u.setRole(UserRole.USER);
        return userRepository.save(u);
    }

    public Stock saveStock(Product product, Location location, int quantity) {
        Stock s = new Stock();
        s.setProduct(product);
        s.setLocation(location);
        s.setQuantity(quantity);
        return stockRepository.save(s);
    }

    public void deleteAll() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        stockRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Transactional
    public List<Stock> findStocksForProducts(List<UUID> productIds) {
        return stockRepository.findByProductIdIn(productIds);
    }

    @Transactional(readOnly = true)
    public Order findOrderWithDetails(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.getOrderDetails().forEach(d -> {
            d.getProduct().getId();
            d.getShippedFrom().getId();
        });
        return order;
    }
}
