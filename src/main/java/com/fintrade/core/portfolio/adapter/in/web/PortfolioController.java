package com.fintrade.core.portfolio.adapter.in.web;

import com.fintrade.core.portfolio.application.port.in.AddStockToWatchlistUseCase;
import com.fintrade.core.portfolio.application.port.in.GetPortfolioUseCase;
import com.fintrade.core.portfolio.application.port.in.UpsertPortfolioCashUseCase;
import com.fintrade.core.portfolio.application.port.in.UpsertPositionUseCase;
import com.fintrade.core.portfolio.application.query.PortfolioView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PortfolioController {

    private final GetPortfolioUseCase getPortfolioUseCase;
    private final UpsertPortfolioCashUseCase upsertPortfolioCashUseCase;
    private final UpsertPositionUseCase upsertPositionUseCase;
    private final AddStockToWatchlistUseCase addStockToWatchlistUseCase;

    public PortfolioController(
            GetPortfolioUseCase getPortfolioUseCase,
            UpsertPortfolioCashUseCase upsertPortfolioCashUseCase,
            UpsertPositionUseCase upsertPositionUseCase,
            AddStockToWatchlistUseCase addStockToWatchlistUseCase
    ) {
        this.getPortfolioUseCase = getPortfolioUseCase;
        this.upsertPortfolioCashUseCase = upsertPortfolioCashUseCase;
        this.upsertPositionUseCase = upsertPositionUseCase;
        this.addStockToWatchlistUseCase = addStockToWatchlistUseCase;
    }

    @GetMapping("/investors/portfolios/{userId}")
    public PortfolioView getPortfolio(@PathVariable UUID userId) {
        return getPortfolioUseCase.handle(userId);
    }

    @PutMapping("/admin/portfolios/{userId}/cash")
    public void upsertCash(@PathVariable UUID userId, @Valid @RequestBody UpdateCashRequest request) {
        upsertPortfolioCashUseCase.handle(new UpsertPortfolioCashUseCase.UpsertPortfolioCashCommand(
                userId,
                request.currency(),
                request.cashBalance()
        ));
    }

    @PutMapping("/admin/portfolios/{userId}/positions/{stockId}")
    public void upsertPosition(@PathVariable UUID userId, @PathVariable UUID stockId, @Valid @RequestBody UpdatePositionRequest request) {
        upsertPositionUseCase.handle(new UpsertPositionUseCase.UpsertPositionCommand(
                userId,
                stockId,
                request.quantity(),
                request.averageCost(),
                request.marketPrice()
        ));
    }

    @PostMapping("/investors/portfolios/{userId}/watchlist/{stockId}")
    public void addToWatchlist(@PathVariable UUID userId, @PathVariable UUID stockId) {
        addStockToWatchlistUseCase.handle(new AddStockToWatchlistUseCase.AddStockToWatchlistCommand(userId, stockId));
    }

    public record UpdateCashRequest(
            @NotNull String currency,
            @NotNull @DecimalMin("0.00") BigDecimal cashBalance
    ) {
    }

    public record UpdatePositionRequest(
            @NotNull @DecimalMin("0.00") BigDecimal quantity,
            @NotNull @DecimalMin("0.00") BigDecimal averageCost,
            @NotNull @DecimalMin("0.00") BigDecimal marketPrice
    ) {
    }
}
