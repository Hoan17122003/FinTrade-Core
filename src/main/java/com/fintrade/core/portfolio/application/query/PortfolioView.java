package com.fintrade.core.portfolio.application.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PortfolioView(
        UUID userId,
        String currency,
        BigDecimal cashBalance,
        BigDecimal stockValue,
        BigDecimal totalValue,
        BigDecimal unrealizedPnl,
        List<PortfolioHoldingView> holdings,
        List<AllocationView> sectorAllocations,
        List<String> watchlistTickers
) {
}
