package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.LocationSelectionResult;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.exception.InsufficientStockException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MostAbundantStrategy implements LocationSelectionStrategy {

    @Override
    public List<LocationSelectionResult> selectLocations(Map<UUID, Integer> productQuantities, List<Stock> stocks) {
        // Stocks arrive ordered by quantity DESC; keep the first (highest) per product
        Map<UUID, Stock> bestStockPerProduct = stocks.stream()
                .collect(Collectors.toMap(
                        s -> s.getProduct().getId(),
                        s -> s,
                        (s1, s2) -> s1
                ));

        return productQuantities.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    int required = entry.getValue();
                    Stock best = bestStockPerProduct.get(productId);

                    if (best == null || best.getQuantity() < required) {
                        throw new InsufficientStockException(
                                "Insufficient stock for product: " + productId);
                    }

                    return new LocationSelectionResult(best.getLocation(), best.getProduct(), required);
                })
                .toList();
    }
}
