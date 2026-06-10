package ro.msg.learning.shop.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "shop")
@Validated
public record ShopProperties(

        @NotNull
        @Pattern(
                regexp = "single-location|most-abundant",
                message = "shop.location-strategy must be one of: single-location, most-abundant"
        )
        String locationStrategy
) {}
