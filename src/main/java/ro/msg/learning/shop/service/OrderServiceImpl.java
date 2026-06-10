package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.config.ShopProperties;
import ro.msg.learning.shop.dto.CreateOrderRequest;
import ro.msg.learning.shop.dto.OrderItem;
import ro.msg.learning.shop.entity.Order;
import ro.msg.learning.shop.entity.OrderDetail;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.entity.User;
import ro.msg.learning.shop.exception.ResourceNotFoundException;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.repository.UserRepository;
import ro.msg.learning.shop.dto.LocationSelectionResult;
import ro.msg.learning.shop.strategy.LocationSelectionStrategy;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final LocationSelectionStrategy locationSelectionStrategy;
    private final ShopProperties shopProperties;

    public Order createOrder(CreateOrderRequest request) {
        boolean hasInvalidQuantity = request.products().stream().anyMatch(i -> i.quantity() <= 0);
        if (hasInvalidQuantity) {
            throw new IllegalArgumentException("Order item quantities must be greater than zero");
        }

        Map<UUID, Integer> productQuantities = request.products().stream()
                .collect(Collectors.toMap(OrderItem::productId, OrderItem::quantity, Integer::sum));

        List<UUID> productIds = List.copyOf(productQuantities.keySet());

        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        log.debug("Selecting stock locations using strategy: {}", shopProperties.locationStrategy());
        List<Stock> stocks = stockRepository.findByProductIdInOrderByQuantityDesc(productIds);
        List<LocationSelectionResult> allocations = locationSelectionStrategy.selectLocations(productQuantities, stocks);

        // Deduct stock for each allocation
        Map<String, Stock> stockIndex = stocks.stream()
                .collect(Collectors.toMap(
                        s -> s.getLocation().getId() + ":" + s.getProduct().getId(),
                        s -> s,
                        (s1, s2) -> s1));

        for (LocationSelectionResult allocation : allocations) {
            String key = allocation.location().getId() + ":" + allocation.product().getId();
            Stock stock = stockIndex.get(key);
            int newQuantity = stock.getQuantity() - allocation.quantity();
            if (newQuantity == 0) {
                stockRepository.delete(stock);
            } else {
                stock.setQuantity(newQuantity);
            }
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(request.orderTimestamp());
        order.setAddress(request.deliveryAddress());

        for (LocationSelectionResult allocation : allocations) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(allocation.product());
            detail.setShippedFrom(allocation.location());
            detail.setQuantity(allocation.quantity());
            order.getOrderDetails().add(detail);
        }

        return orderRepository.save(order);
    }
}
