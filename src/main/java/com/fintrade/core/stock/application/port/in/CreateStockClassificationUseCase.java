package com.fintrade.core.stock.application.port.in;

import com.fintrade.core.stock.domain.model.ClassificationType;

import java.util.UUID;

public interface CreateStockClassificationUseCase {

    UUID handle(CreateStockClassificationCommand command);

    record CreateStockClassificationCommand(String code, String name, ClassificationType type, String description) {
    }
}
