package ro.msg.learning.shop.service;

import ro.msg.learning.shop.dto.CreateOrderRequest;
import ro.msg.learning.shop.entity.Order;

public interface OrderService {

    Order createOrder(CreateOrderRequest request);
}
