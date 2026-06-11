package ro.msg.learning.shop.strategy;

import org.junit.jupiter.api.Test;
import ro.msg.learning.shop.dto.LocationSelectionResult;
import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.exception.InsufficientStockException;
import ro.msg.learning.shop.util.TestEntityBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MostAbundantStrategyTest {

    private final MostAbundantStrategy strategy = new MostAbundantStrategy();

    @Test
    void selectLocations_picksLocationWithHighestStock() {
        UUID productId = UUID.randomUUID();
        Location lowStock = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Location highStock = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Product product = TestEntityBuilder.buildProduct(productId);

        List<Stock> stocks = List.of(
                TestEntityBuilder.buildStock(highStock, product, 50),
                TestEntityBuilder.buildStock(lowStock, product, 10)
        );

        List<LocationSelectionResult> result = strategy.selectLocations(Map.of(productId, 5), stocks);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).location()).isEqualTo(highStock);
        assertThat(result.get(0).quantity()).isEqualTo(5);
    }

    @Test
    void selectLocations_handlesMultipleProductsFromDifferentLocations() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Location loc1 = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Location loc2 = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Product p1 = TestEntityBuilder.buildProduct(productId1);
        Product p2 = TestEntityBuilder.buildProduct(productId2);

        List<Stock> stocks = List.of(
                TestEntityBuilder.buildStock(loc1, p1, 30),
                TestEntityBuilder.buildStock(loc2, p2, 20)
        );

        List<LocationSelectionResult> result = strategy.selectLocations(Map.of(productId1, 5, productId2, 3), stocks);

        assertThat(result).hasSize(2);
        assertThat(result).anyMatch(r -> r.location().equals(loc1) && r.quantity() == 5);
        assertThat(result).anyMatch(r -> r.location().equals(loc2) && r.quantity() == 3);
    }

    @Test
    void selectLocations_picksHighestStock_evenWhenInputIsUnsorted() {
        UUID productId = UUID.randomUUID();
        Location lowStock = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Location highStock = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Product product = TestEntityBuilder.buildProduct(productId);

        // low-stock entry comes first — strategy must not just pick the first
        List<Stock> stocks = List.of(
                TestEntityBuilder.buildStock(lowStock, product, 5),
                TestEntityBuilder.buildStock(highStock, product, 50)
        );

        List<LocationSelectionResult> result = strategy.selectLocations(Map.of(productId, 3), stocks);

        assertThat(result.get(0).location()).isEqualTo(highStock);
    }

    @Test
    void selectLocations_throwsInsufficientStock_whenNoStockForProduct() {
        UUID productId = UUID.randomUUID();

        assertThatThrownBy(() -> strategy.selectLocations(Map.of(productId, 5), List.of()))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void selectLocations_throwsInsufficientStock_whenBestStockBelowRequired() {
        UUID productId = UUID.randomUUID();
        Location location = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Product product = TestEntityBuilder.buildProduct(productId);

        List<Stock> stocks = List.of(TestEntityBuilder.buildStock(location, product, 3));

        assertThatThrownBy(() -> strategy.selectLocations(Map.of(productId, 10), stocks))
                .isInstanceOf(InsufficientStockException.class);
    }
}
