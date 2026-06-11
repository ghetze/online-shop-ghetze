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

class SingleLocationStrategyTest {

    private final SingleLocationStrategy strategy = new SingleLocationStrategy();

    @Test
    void selectLocations_returnsAllocation_whenSingleLocationHasAllProducts() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Location location = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Product p1 = TestEntityBuilder.buildProduct(productId1);
        Product p2 = TestEntityBuilder.buildProduct(productId2);

        List<Stock> stocks = List.of(
                TestEntityBuilder.buildStock(location, p1, 10),
                TestEntityBuilder.buildStock(location, p2, 5)
        );

        List<LocationSelectionResult> result = strategy.selectLocations(Map.of(productId1, 3, productId2, 2), stocks);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.location().equals(location));
    }

    @Test
    void selectLocations_throwsInsufficientStock_whenNoSingleLocationHasAll() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Location loc1 = TestEntityBuilder.buildLocation(UUID.randomUUID());
        Location loc2 = TestEntityBuilder.buildLocation(UUID.randomUUID());

        List<Stock> stocks = List.of(
                TestEntityBuilder.buildStock(loc1, TestEntityBuilder.buildProduct(productId1), 10),
                TestEntityBuilder.buildStock(loc2, TestEntityBuilder.buildProduct(productId2), 10)
        );

        assertThatThrownBy(() -> strategy.selectLocations(Map.of(productId1, 5, productId2, 5), stocks))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void selectLocations_throwsInsufficientStock_whenQuantityInsufficient() {
        UUID productId = UUID.randomUUID();
        Location location = TestEntityBuilder.buildLocation(UUID.randomUUID());

        List<Stock> stocks = List.of(TestEntityBuilder.buildStock(location, TestEntityBuilder.buildProduct(productId), 2));

        assertThatThrownBy(() -> strategy.selectLocations(Map.of(productId, 10), stocks))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void selectLocations_picksFirstLocationByIdWhenMultipleQualify() {
        UUID productId = UUID.randomUUID();
        UUID locId1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID locId2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
        Location loc1 = TestEntityBuilder.buildLocation(locId1);
        Location loc2 = TestEntityBuilder.buildLocation(locId2);
        Product product = TestEntityBuilder.buildProduct(productId);

        List<Stock> stocks = List.of(
                TestEntityBuilder.buildStock(loc1, product, 20),
                TestEntityBuilder.buildStock(loc2, product, 20)
        );

        List<LocationSelectionResult> result = strategy.selectLocations(Map.of(productId, 5), stocks);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).location().getId()).isEqualTo(locId1);
    }
}
