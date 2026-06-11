package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.LocationSelectionResult;
import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.exception.InsufficientStockException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SingleLocationStrategy implements LocationSelectionStrategy {

    @Override
    public List<LocationSelectionResult> selectLocations(Map<UUID, Integer> productQuantities, List<Stock> stocks) {
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
                .min(java.util.Comparator.comparing(Location::getId))
                .orElseThrow(() -> new InsufficientStockException(
                        "No single location has all required products in sufficient quantities"));

        Map<UUID, Stock> stockByProduct = stocksByLocation.get(selectedLocation.getId()).stream()
                .collect(Collectors.toMap(
                        s -> s.getProduct().getId(),
                        s -> s,
                        (s1, s2) -> s1.getQuantity() >= s2.getQuantity() ? s1 : s2));

        return productQuantities.entrySet().stream()
                .map(entry -> new LocationSelectionResult(
                        selectedLocation,
                        stockByProduct.get(entry.getKey()).getProduct(),
                        entry.getValue()))
                .toList();
    }
}
