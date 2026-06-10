package ro.msg.learning.shop.dto;

import ro.msg.learning.shop.entity.Location;
import ro.msg.learning.shop.entity.Product;

public record LocationSelectionResult(Location location, Product product, int quantity) {}
