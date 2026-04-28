package com.fintrade.core.portfolio.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface UpsertPortfolioCashUseCase {

    void handle(UpsertPortfolioCashCommand command);

    record UpsertPortfolioCashCommand(UUID userId, String currency, BigDecimal cashBalance) {
    }
}
