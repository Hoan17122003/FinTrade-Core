package com.fintrade.core.stock.application.port.out;

import com.fintrade.core.stock.application.port.in.SearchStocksUseCase.StockFilter;
import com.fintrade.core.stock.domain.model.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {

    Stock save(Stock stock);

    Optional<Stock> findById(UUID id);

    List<Stock> search(StockFilter filter);
}
