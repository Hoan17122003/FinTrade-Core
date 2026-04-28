package com.fintrade.core.order.application.port.in;

import com.fintrade.core.order.application.query.OrderView;
import com.fintrade.core.order.domain.model.OrderSide;
import com.fintrade.core.order.domain.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface SearchOrdersUseCase {

    List<OrderView> handle(OrderFilter filter);

    record OrderFilter(UUID userId, UUID stockId, OrderSide side, OrderStatus status) {
    }
}
