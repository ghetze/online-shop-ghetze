package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.CreateOrderRequest;
import ro.msg.learning.shop.dto.OrderItem;
import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Order;
import ro.msg.learning.shop.entity.OrderDetail;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.entity.User;
import ro.msg.learning.shop.exception.InsufficientStockException;
import ro.msg.learning.shop.exception.ResourceNotFoundException;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    public Order createOrder(CreateOrderRequest request) {
        // Validate quantities
        boolean hasInvalidQuantity = request.products().stream().anyMatch(i -> i.quantity() <= 0);
        if (hasInvalidQuantity) {
            throw new IllegalArgumentException("Order item quantities must be greater than zero");
        }

        // Merge duplicate productIds by summing quantities
        Map<UUID, Integer> productQuantities = request.products().stream()
                .collect(Collectors.toMap(OrderItem::productId, OrderItem::quantity, Integer::sum));

        List<UUID> productIds = List.copyOf(productQuantities.keySet());

        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        List<Stock> stocks = stockRepository.findByProductIdIn(productIds);

        Map<UUID, List<Stock>> stocksByLocation = stocks.stream()
                .collect(Collectors.groupingBy(s -> s.getLocation().getId()));

        Location selectedLocation = stocksByLocation.entrySet().stream()
                .filter(entry -> {
                    Map<UUID, Integer> locationStock = entry.getValue().stream()
                            .collect(Collectors.toMap(
                                    s -> s.getProduct().getId(),
                                    Stock::getQuantity,
                                    Integer::sum));
                    return productQuantities.entrySet().stream()
                            .allMatch(req -> locationStock.getOrDefault(req.getKey(), 0) >= req.getValue());
                })
                .map(entry -> entry.getValue().get(0).getLocation())
                .min(Comparator.comparing(Location::getId))
                .orElseThrow(() -> new InsufficientStockException(
                        "No single location has all required products in sufficient quantities"));

        List<Stock> locationStocks = stocksByLocation.get(selectedLocation.getId());
        for (Stock stock : locationStocks) {
            UUID productId = stock.getProduct().getId();
            Integer requiredQty = productQuantities.get(productId);
            if (requiredQty != null) {
                int newQuantity = stock.getQuantity() - requiredQty;
                if (newQuantity == 0) {
                    stockRepository.delete(stock);
                } else {
                    stock.setQuantity(newQuantity);
                }
            }
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(request.orderTimestamp());
        order.setAddress(request.deliveryAddress());

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p));

        for (OrderItem item : request.products()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(productMap.get(item.productId()));
            detail.setShippedFrom(selectedLocation);
            detail.setQuantity(item.quantity());
            order.getOrderDetails().add(detail);
        }

        return orderRepository.save(order);
    }
}
