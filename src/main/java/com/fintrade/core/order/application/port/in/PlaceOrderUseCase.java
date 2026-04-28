package com.fintrade.core.order.application.port.in;

import com.fintrade.core.order.domain.model.OrderSide;

import java.math.BigDecimal;
import java.util.UUID;

public interface PlaceOrderUseCase {

    PlaceOrderResult handle(PlaceOrderCommand command);

    record PlaceOrderCommand(
            UUID userId,
            UUID stockId,
            OrderSide side,
            BigDecimal quantity,
            BigDecimal limitPrice
    ) {
    }

    record PlaceOrderResult(UUID orderId, UUID cashLedgerEntryId, UUID securitiesLedgerEntryId, UUID feeEntryId) {
    }
}
