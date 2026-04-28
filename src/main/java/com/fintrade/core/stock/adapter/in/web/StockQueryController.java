package com.fintrade.core.stock.adapter.in.web;

import com.fintrade.core.stock.application.port.in.GetStockDetailUseCase;
import com.fintrade.core.stock.application.port.in.SearchStocksUseCase;
import com.fintrade.core.stock.application.query.StockDetailView;
import com.fintrade.core.stock.domain.model.TradingStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stocks")
public class StockQueryController {

    private final SearchStocksUseCase searchStocksUseCase;
    private final GetStockDetailUseCase getStockDetailUseCase;

    public StockQueryController(SearchStocksUseCase searchStocksUseCase, GetStockDetailUseCase getStockDetailUseCase) {
        this.searchStocksUseCase = searchStocksUseCase;
        this.getStockDetailUseCase = getStockDetailUseCase;
    }

    @GetMapping
    public List<StockDetailView> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String market,
            @RequestParam(required = false) TradingStatus tradingStatus,
            @RequestParam(required = false) String classificationCode,
            @RequestParam(required = false) String indexCode,
            @RequestParam(required = false) UUID issuerCompanyId
    ) {
        return searchStocksUseCase.handle(new SearchStocksUseCase.StockFilter(
                keyword,
                market,
                tradingStatus,
                classificationCode,
                indexCode,
                issuerCompanyId
        ));
    }

    @GetMapping("/{stockId}")
    public StockDetailView getById(@PathVariable UUID stockId) {
        return getStockDetailUseCase.handle(stockId);
    }
}
