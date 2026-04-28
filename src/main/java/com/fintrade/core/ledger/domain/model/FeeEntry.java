package com.fintrade.core.ledger.domain.model;

import com.fintrade.core.shared.domain.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record FeeEntry(
        UUID id,
        UUID orderId,
        UUID userId,
        String feeCode,
        Money amount,
        Instant createdAt
) {

    public FeeEntry {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(feeCode, "feeCode must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static FeeEntry create(UUID orderId, UUID userId, String feeCode, Money amount) {
        return new FeeEntry(UUID.randomUUID(), orderId, userId, feeCode, amount, Instant.now());
    }
}
