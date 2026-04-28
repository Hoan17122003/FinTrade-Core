package com.fintrade.core.ledger.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record SecuritiesLedgerEntry(
        UUID id,
        UUID orderId,
        UUID userId,
        UUID stockId,
        String ticker,
        SecuritiesLedgerEntryType entryType,
        BigDecimal quantity,
        Instant createdAt
) {

    public SecuritiesLedgerEntry {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(stockId, "stockId must not be null");
        Objects.requireNonNull(ticker, "ticker must not be null");
        Objects.requireNonNull(entryType, "entryType must not be null");
        Objects.requireNonNull(quantity, "quantity must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static SecuritiesLedgerEntry create(
            UUID orderId,
            UUID userId,
            UUID stockId,
            String ticker,
            SecuritiesLedgerEntryType entryType,
            BigDecimal quantity
    ) {
        return new SecuritiesLedgerEntry(UUID.randomUUID(), orderId, userId, stockId, ticker, entryType, quantity, Instant.now());
    }
}
