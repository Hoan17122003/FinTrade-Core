package com.fintrade.core.portfolio.application.port.in;

import java.util.UUID;

public interface AddStockToWatchlistUseCase {

    void handle(AddStockToWatchlistCommand command);

    record AddStockToWatchlistCommand(UUID userId, UUID stockId) {
    }
}
