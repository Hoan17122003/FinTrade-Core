package com.fintrade.core.ledger.application.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryView(
        UUID id,
        String stream,
        String entryType,
        UUID orderId,
        UUID stockId,
        String ticker,
        String feeCode,
        BigDecimal quantity,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {
}
