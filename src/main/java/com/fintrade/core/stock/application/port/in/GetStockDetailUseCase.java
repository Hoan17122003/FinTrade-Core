package com.fintrade.core.stock.application.port.in;

import com.fintrade.core.stock.application.query.StockDetailView;

import java.util.UUID;

public interface GetStockDetailUseCase {

    StockDetailView handle(UUID stockId);
}
