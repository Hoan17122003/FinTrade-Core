package com.fintrade.core.portfolio.application.service;

import com.fintrade.core.portfolio.application.port.in.AddStockToWatchlistUseCase;
import com.fintrade.core.portfolio.application.port.in.GetPortfolioUseCase;
import com.fintrade.core.portfolio.application.port.in.UpsertPortfolioCashUseCase;
import com.fintrade.core.portfolio.application.port.in.UpsertPositionUseCase;
import com.fintrade.core.portfolio.application.port.out.PortfolioRepository;
import com.fintrade.core.portfolio.application.query.AllocationView;
import com.fintrade.core.portfolio.application.query.PortfolioHoldingView;
import com.fintrade.core.portfolio.application.query.PortfolioView;
import com.fintrade.core.portfolio.domain.model.Portfolio;
import com.fintrade.core.portfolio.domain.model.Position;
import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.shared.domain.Money;
import com.fintrade.core.stock.application.port.out.StockClassificationRepository;
import com.fintrade.core.stock.application.port.out.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PortfolioApplicationService implements
        GetPortfolioUseCase,
        UpsertPortfolioCashUseCase,
        UpsertPositionUseCase,
        AddStockToWatchlistUseCase {

    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final StockClassificationRepository stockClassificationRepository;

    public PortfolioApplicationService(
            PortfolioRepository portfolioRepository,
            StockRepository stockRepository,
            StockClassificationRepository stockClassificationRepository
    ) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.stockClassificationRepository = stockClassificationRepository;
    }

    @Override
    public PortfolioView handle(UUID userId) {
        var portfolio = portfolioRepository.findByUserId(userId)
                .orElse(Portfolio.empty(userId, "VND"));

        var holdingViews = new ArrayList<PortfolioHoldingView>();
        var sectorAllocations = new LinkedHashMap<String, BigDecimal>();
        BigDecimal stockValue = BigDecimal.ZERO;
        BigDecimal unrealizedPnl = BigDecimal.ZERO;

        for (var position : portfolio.positions()) {
            var stock = stockRepository.findById(position.stockId())
                    .orElseThrow(() -> new DomainException("Stock not found for position"));
            var classifications = stockClassificationRepository.findByIds(stock.classificationIds());
            var marketValue = position.marketValue().amount();
            stockValue = stockValue.add(marketValue);
            unrealizedPnl = unrealizedPnl.add(position.unrealizedPnl().amount());

            var labels = classifications.stream()
                    .map(item -> item.code() + ":" + item.name())
                    .toList();

            classifications.stream()
                    .filter(item -> item.type() == com.fintrade.core.stock.domain.model.ClassificationType.SECTOR)
                    .forEach(item -> sectorAllocations.merge(item.name(), marketValue, BigDecimal::add));

            holdingViews.add(new PortfolioHoldingView(
                    position.stockId(),
                    position.ticker(),
                    position.quantity(),
                    position.averageCost().amount(),
                    position.marketPrice().amount(),
                    marketValue,
                    position.unrealizedPnl().amount(),
                    labels
            ));
        }

        var totalValue = portfolio.cashBalance().amount().add(stockValue);
        var allocations = sectorAllocations.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .map(entry -> new AllocationView(
                        entry.getKey(),
                        entry.getValue(),
                        totalValue.signum() == 0
                                ? BigDecimal.ZERO
                                : entry.getValue().divide(totalValue, 4, RoundingMode.HALF_UP)
                ))
                .toList();

        var watchlistTickers = portfolio.watchlist().stockIds().stream()
                .map(stockId -> stockRepository.findById(stockId).map(com.fintrade.core.stock.domain.model.Stock::ticker).orElse("UNKNOWN"))
                .sorted(Comparator.naturalOrder())
                .toList();

        return new PortfolioView(
                userId,
                portfolio.cashBalance().currency(),
                portfolio.cashBalance().amount(),
                stockValue,
                totalValue,
                unrealizedPnl,
                holdingViews,
                allocations,
                watchlistTickers
        );
    }

    @Override
    public void handle(UpsertPortfolioCashCommand command) {
        var existing = portfolioRepository.findByUserId(command.userId())
                .orElse(Portfolio.empty(command.userId(), command.currency()));
        var updated = new Portfolio(
                existing.userId(),
                new Money(command.currency(), command.cashBalance()),
                existing.positions(),
                existing.watchlist()
        );
        portfolioRepository.save(updated);
    }

    @Override
    public void handle(UpsertPositionCommand command) {
        var existing = portfolioRepository.findByUserId(command.userId())
                .orElse(Portfolio.empty(command.userId(), "VND"));
        var stock = stockRepository.findById(command.stockId())
                .orElseThrow(() -> new DomainException("Stock not found"));

        var positions = new ArrayList<>(existing.positions());
        positions.removeIf(item -> item.stockId().equals(command.stockId()));
        positions.add(new Position(
                command.stockId(),
                stock.ticker(),
                command.quantity(),
                new Money(stock.currency(), command.averageCost()),
                new Money(stock.currency(), command.marketPrice())
        ));

        portfolioRepository.save(new Portfolio(existing.userId(), existing.cashBalance(), positions, existing.watchlist()));
    }

    @Override
    public void handle(AddStockToWatchlistCommand command) {
        var existing = portfolioRepository.findByUserId(command.userId())
                .orElse(Portfolio.empty(command.userId(), "VND"));
        stockRepository.findById(command.stockId()).orElseThrow(() -> new DomainException("Stock not found"));
        portfolioRepository.save(new Portfolio(
                existing.userId(),
                existing.cashBalance(),
                existing.positions(),
                existing.watchlist().addStock(command.stockId())
        ));
    }
}
