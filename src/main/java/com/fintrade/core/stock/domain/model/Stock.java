package com.fintrade.core.stock.domain.model;

import com.fintrade.core.shared.domain.DomainException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Stock(
        UUID id,
        String ticker,
        String displayName,
        UUID issuerCompanyId,
        BigDecimal parValue,
        int lotSize,
        String currency,
        String market,
        StockLifecycleStatus listingStatus,
        TradingStatus tradingStatus,
        LocalDate listingDate,
        Set<UUID> classificationIds,
        Set<UUID> indexBasketIds
) {

    public Stock {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(ticker, "ticker must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(issuerCompanyId, "issuerCompanyId must not be null");
        Objects.requireNonNull(parValue, "parValue must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        Objects.requireNonNull(market, "market must not be null");
        Objects.requireNonNull(listingStatus, "listingStatus must not be null");
        Objects.requireNonNull(tradingStatus, "tradingStatus must not be null");
        Objects.requireNonNull(listingDate, "listingDate must not be null");
        classificationIds = Set.copyOf(classificationIds);
        indexBasketIds = Set.copyOf(indexBasketIds);
        if (ticker.isBlank() || displayName.isBlank() || currency.isBlank() || market.isBlank()) {
            throw new DomainException("Stock fields must not be blank");
        }
        if (parValue.signum() <= 0 || lotSize <= 0) {
            throw new DomainException("Stock par value and lot size must be positive");
        }
    }

    public static Stock createApproved(
            UUID issuerCompanyId,
            String ticker,
            String displayName,
            BigDecimal parValue,
            int lotSize,
            String currency,
            String market
    ) {
        return new Stock(
                UUID.randomUUID(),
                ticker,
                displayName,
                issuerCompanyId,
                parValue,
                lotSize,
                currency,
                market,
                StockLifecycleStatus.APPROVED,
                TradingStatus.PENDING,
                LocalDate.now(),
                Set.of(),
                Set.of()
        );
    }

    public Stock activate() {
        return new Stock(
                id,
                ticker,
                displayName,
                issuerCompanyId,
                parValue,
                lotSize,
                currency,
                market,
                StockLifecycleStatus.ACTIVE,
                TradingStatus.TRADING,
                listingDate,
                classificationIds,
                indexBasketIds
        );
    }

    public Stock addClassification(UUID classificationId) {
        var updated = new LinkedHashSet<>(classificationIds);
        updated.add(classificationId);
        return new Stock(id, ticker, displayName, issuerCompanyId, parValue, lotSize, currency, market,
                listingStatus, tradingStatus, listingDate, updated, indexBasketIds);
    }

    public Stock addIndexBasket(UUID basketId) {
        var updated = new LinkedHashSet<>(indexBasketIds);
        updated.add(basketId);
        return new Stock(id, ticker, displayName, issuerCompanyId, parValue, lotSize, currency, market,
                listingStatus, tradingStatus, listingDate, classificationIds, updated);
    }
}
