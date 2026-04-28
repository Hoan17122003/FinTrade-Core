package com.fintrade.core.order.application.query;

import com.fintrade.core.order.domain.model.OrderSide;
import com.fintrade.core.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderView(
        UUID id,
        UUID userId,
        UUID stockId,
        String ticker,
        OrderSide side,
        BigDecimal quantity,
        BigDecimal limitPrice,
        String currency,
        OrderStatus status,
        Instant submittedAt,
        Instant filledAt
) {
}
