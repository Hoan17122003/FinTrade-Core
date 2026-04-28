package com.fintrade.core.order.domain.model;

import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.shared.domain.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Order(
        UUID id,
        UUID userId,
        UUID stockId,
        String ticker,
        OrderSide side,
        BigDecimal quantity,
        Money price,
        OrderStatus status,
        String rejectionReason,
        Instant submittedAt,
        Instant acceptedAt,
        Instant filledAt
) {

    public Order {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(stockId, "stockId must not be null");
        Objects.requireNonNull(ticker, "ticker must not be null");
        Objects.requireNonNull(side, "side must not be null");
        Objects.requireNonNull(quantity, "quantity must not be null");
        Objects.requireNonNull(price, "price must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(submittedAt, "submittedAt must not be null");
        if (ticker.isBlank()) {
            throw new DomainException("Ticker must not be blank");
        }
        if (quantity.signum() <= 0) {
            throw new DomainException("Quantity must be positive");
        }
    }

    public static Order submit(UUID userId, UUID stockId, String ticker, OrderSide side, BigDecimal quantity, Money price) {
        return new Order(
                UUID.randomUUID(),
                userId,
                stockId,
                ticker,
                side,
                quantity,
                price,
                OrderStatus.SUBMITTED,
                null,
                Instant.now(),
                null,
                null
        );
    }

    public Order accept() {
        if (status != OrderStatus.SUBMITTED) {
            throw new DomainException("Only submitted orders can be accepted");
        }
        return new Order(id, userId, stockId, ticker, side, quantity, price, OrderStatus.ACCEPTED,
                null, submittedAt, Instant.now(), null);
    }

    public Order fill() {
        if (status != OrderStatus.ACCEPTED) {
            throw new DomainException("Only accepted orders can be filled");
        }
        return new Order(id, userId, stockId, ticker, side, quantity, price, OrderStatus.FILLED,
                null, submittedAt, acceptedAt, Instant.now());
    }

    public Money grossAmount() {
        return price.multiply(quantity);
    }
}
