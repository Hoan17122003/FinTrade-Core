package com.fintrade.core.stock.application.port.in;

import java.util.UUID;

public interface AssignStockClassificationUseCase {

    void handle(AssignStockClassificationCommand command);

    record AssignStockClassificationCommand(UUID stockId, UUID classificationId) {
    }
}
