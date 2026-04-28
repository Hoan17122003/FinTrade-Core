package com.fintrade.core.portfolio.application.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PortfolioHoldingView(
        UUID stockId,
        String ticker,
        BigDecimal quantity,
        BigDecimal averageCost,
        BigDecimal marketPrice,
        BigDecimal marketValue,
        BigDecimal unrealizedPnl,
        List<String> classifications
) {
}
