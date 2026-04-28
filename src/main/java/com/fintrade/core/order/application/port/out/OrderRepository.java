package com.fintrade.core.order.application.port.out;

import com.fintrade.core.order.application.port.in.SearchOrdersUseCase.OrderFilter;
import com.fintrade.core.order.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> search(OrderFilter filter);
}
