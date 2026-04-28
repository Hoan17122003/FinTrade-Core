package com.fintrade.core.stock.application.query;

import com.fintrade.core.stock.domain.model.StockLifecycleStatus;
import com.fintrade.core.stock.domain.model.TradingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StockDetailView(
        UUID id,
        String ticker,
        String displayName,
        String issuerCompanyName,
        String primarySector,
        String market,
        StockLifecycleStatus listingStatus,
        TradingStatus tradingStatus,
        LocalDate listingDate,
        List<String> classifications,
        List<String> indexBaskets
) {
}
