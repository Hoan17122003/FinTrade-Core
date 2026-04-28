package com.fintrade.core.stock.application.port.in;

import com.fintrade.core.stock.application.query.StockDetailView;
import com.fintrade.core.stock.domain.model.TradingStatus;

import java.util.List;
import java.util.UUID;

public interface SearchStocksUseCase {

    List<StockDetailView> handle(StockFilter filter);

    record StockFilter(
            String keyword,
            String market,
            TradingStatus tradingStatus,
            String classificationCode,
            String indexCode,
            UUID issuerCompanyId
    ) {
    }
}
