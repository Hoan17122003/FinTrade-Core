package com.fintrade.core.portfolio.application.query;

import java.math.BigDecimal;

public record AllocationView(String key, BigDecimal marketValue, BigDecimal ratio) {
}
