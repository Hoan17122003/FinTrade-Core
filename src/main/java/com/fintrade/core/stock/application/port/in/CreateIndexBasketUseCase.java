package com.fintrade.core.stock.application.port.in;

import java.util.UUID;

public interface CreateIndexBasketUseCase {

    UUID handle(CreateIndexBasketCommand command);

    record CreateIndexBasketCommand(String code, String name, String description) {
    }
}
