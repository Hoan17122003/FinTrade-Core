package com.fintrade.core.portfolio.domain.model;

import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.shared.domain.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record Position(
        UUID stockId,
        String ticker,
        BigDecimal quantity,
        Money averageCost,
        Money marketPrice
) {

    public Position {
        Objects.requireNonNull(stockId, "stockId must not be null");
        Objects.requireNonNull(ticker, "ticker must not be null");
        Objects.requireNonNull(quantity, "quantity must not be null");
        Objects.requireNonNull(averageCost, "averageCost must not be null");
        Objects.requireNonNull(marketPrice, "marketPrice must not be null");
        if (ticker.isBlank()) {
            throw new DomainException("Ticker must not be blank");
        }
        if (quantity.signum() < 0) {
            throw new DomainException("Quantity must not be negative");
        }
    }

    public Money marketValue() {
        return marketPrice.multiply(quantity);
    }

    public Money costBasis() {
        return averageCost.multiply(quantity);
    }

    public Money unrealizedPnl() {
        return marketValue().subtract(costBasis());
    }
}
