package com.fintrade.core.shared.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(String currency, BigDecimal amount) {

    public Money {
        Objects.requireNonNull(currency, "currency must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        if (currency.isBlank()) {
            throw new DomainException("Currency must not be blank");
        }
    }

    public static Money zero(String currency) {
        return new Money(currency, BigDecimal.ZERO);
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(currency, amount.add(other.amount));
    }

    public Money subtract(Money other) {
        ensureSameCurrency(other);
        return new Money(currency, amount.subtract(other.amount));
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(currency, amount.multiply(multiplier));
    }

    private void ensureSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new DomainException("Currency mismatch");
        }
    }
}
