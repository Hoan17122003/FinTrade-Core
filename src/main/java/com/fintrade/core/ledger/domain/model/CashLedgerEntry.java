package com.fintrade.core.ledger.domain.model;

import com.fintrade.core.shared.domain.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record CashLedgerEntry(
        UUID id,
        UUID orderId,
        UUID userId,
        CashLedgerEntryType entryType,
        Money amount,
        Instant createdAt
) {

    public CashLedgerEntry {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(entryType, "entryType must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static CashLedgerEntry create(UUID orderId, UUID userId, CashLedgerEntryType entryType, Money amount) {
        return new CashLedgerEntry(UUID.randomUUID(), orderId, userId, entryType, amount, Instant.now());
    }
}
