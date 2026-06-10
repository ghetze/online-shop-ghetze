package ro.msg.learning.shop.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.strategy.LocationSelectionStrategy;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

@Configuration
@EnableConfigurationProperties(ShopProperties.class)
public class LocationStrategyConfig {

    @Bean
    public LocationSelectionStrategy locationSelectionStrategy(ShopProperties props) {
        return switch (props.locationStrategy()) {
            case "single-location" -> new SingleLocationStrategy();
            case "most-abundant"   -> new MostAbundantStrategy();
            default -> throw new IllegalStateException(
                    "Unknown location strategy: " + props.locationStrategy());
        };
    }
}
