package com.fintrade.core.portfolio.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface UpsertPositionUseCase {

    void handle(UpsertPositionCommand command);

    record UpsertPositionCommand(
            UUID userId,
            UUID stockId,
            BigDecimal quantity,
            BigDecimal averageCost,
            BigDecimal marketPrice
    ) {
    }
}
