package com.fintrade.core.stock.application.port.in;

import java.util.UUID;

public interface AddStockToIndexBasketUseCase {

    void handle(AddStockToIndexBasketCommand command);

    record AddStockToIndexBasketCommand(UUID basketId, UUID stockId) {
    }
}
