package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.LocationSelectionResult;
import ro.msg.learning.shop.entity.Stock;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface LocationSelectionStrategy {

    /**
     * Selects the source locations for each product in the order.
     *
     * @param productQuantities map of productId → required quantity (already merged/deduplicated)
     * @param stocks            all stocks for the required products, pre-fetched and locked by the caller
     * @return list of {location, product, quantity} allocations
     * @throws ro.msg.learning.shop.exception.InsufficientStockException if no valid allocation exists
     */
    List<LocationSelectionResult> selectLocations(Map<UUID, Integer> productQuantities, List<Stock> stocks);
}
