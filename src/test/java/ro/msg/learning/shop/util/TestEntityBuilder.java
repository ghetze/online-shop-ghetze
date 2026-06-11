package ro.msg.learning.shop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;
import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Product;
import ro.msg.learning.shop.entity.ProductCategory;import ro.msg.learning.shop.entity.Stock;
import ro.msg.learning.shop.entity.embeddable.Address;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestEntityBuilder {

    public static ProductCategory buildCategory(UUID id) {
        ProductCategory c = new ProductCategory();
        ReflectionTestUtils.setField(c, "id", id);
        c.setName("Category-" + id);
        return c;
    }

    public static Product buildProduct(UUID id) {
        Product p = new Product();
        ReflectionTestUtils.setField(p, "id", id);
        p.setName("Product-" + id);
        p.setPrice(BigDecimal.ONE);
        p.setWeight(1.0);
        p.setCategory(buildCategory(UUID.randomUUID()));
        return p;
    }

    public static Product buildProduct(UUID id, ProductCategory category) {
        Product p = new Product();
        ReflectionTestUtils.setField(p, "id", id);
        p.setName("Product-" + id);
        p.setPrice(BigDecimal.ONE);
        p.setWeight(1.0);
        p.setCategory(category);
        return p;
    }

    public static Product buildProduct(UUID id, String name, BigDecimal price, double weight, String imageUrl) {
        Product p = new Product();
        ReflectionTestUtils.setField(p, "id", id);
        p.setName(name);
        p.setPrice(price);
        p.setWeight(weight);
        p.setImageUrl(imageUrl);
        p.setCategory(buildCategory(UUID.randomUUID()));
        return p;
    }

    public static Location buildLocation(UUID id) {
        Location l = new Location();
        ReflectionTestUtils.setField(l, "id", id);
        l.setName("Location-" + id);
        l.setAddress(new Address("RO", "Cluj", "Cluj", "Str. Main 1"));
        return l;
    }

    public static Stock buildStock(Location location, Product product, int quantity) {
        Stock s = new Stock();
        ReflectionTestUtils.setField(s, "id", UUID.randomUUID());
        s.setLocation(location);
        s.setProduct(product);
        s.setQuantity(quantity);
        return s;
    }
}
