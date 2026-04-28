package com.fintrade.core.portfolio.adapter.out.persistence;

import com.fintrade.core.portfolio.adapter.out.persistence.entity.PortfolioEntity;
import com.fintrade.core.portfolio.adapter.out.persistence.entity.PositionEntity;
import com.fintrade.core.portfolio.adapter.out.persistence.entity.WatchlistItemEntity;
import com.fintrade.core.portfolio.adapter.out.persistence.repository.SpringDataPortfolioRepository;
import com.fintrade.core.portfolio.application.port.out.PortfolioRepository;
import com.fintrade.core.portfolio.domain.model.Portfolio;
import com.fintrade.core.portfolio.domain.model.Position;
import com.fintrade.core.portfolio.domain.model.Watchlist;
import com.fintrade.core.shared.domain.Money;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaPortfolioRepositoryAdapter implements PortfolioRepository {

    private final SpringDataPortfolioRepository repository;

    public JpaPortfolioRepositoryAdapter(SpringDataPortfolioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        var entity = repository.findById(portfolio.userId()).orElseGet(PortfolioEntity::new);
        entity.setUserId(portfolio.userId());
        entity.setBaseCurrency(portfolio.cashBalance().currency());
        entity.setCashBalance(portfolio.cashBalance().amount());

        var positions = new ArrayList<PositionEntity>();
        for (var position : portfolio.positions()) {
            var item = new PositionEntity();
            item.setPortfolio(entity);
            item.setStockId(position.stockId());
            item.setTicker(position.ticker());
            item.setQuantity(position.quantity());
            item.setAverageCost(position.averageCost().amount());
            item.setMarketPrice(position.marketPrice().amount());
            item.setCurrency(position.averageCost().currency());
            positions.add(item);
        }

        var watchlistItems = new ArrayList<WatchlistItemEntity>();
        for (var stockId : portfolio.watchlist().stockIds()) {
            var item = new WatchlistItemEntity();
            item.setPortfolio(entity);
            item.setStockId(stockId);
            watchlistItems.add(item);
        }

        entity.getPositions().clear();
        entity.getPositions().addAll(positions);
        entity.getWatchlistItems().clear();
        entity.getWatchlistItems().addAll(watchlistItems);

        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Portfolio> findByUserId(UUID userId) {
        return repository.findById(userId).map(this::toDomain);
    }

    private Portfolio toDomain(PortfolioEntity entity) {
        return new Portfolio(
                entity.getUserId(),
                new Money(entity.getBaseCurrency(), entity.getCashBalance()),
                entity.getPositions().stream()
                        .map(item -> new Position(
                                item.getStockId(),
                                item.getTicker(),
                                item.getQuantity(),
                                new Money(item.getCurrency(), item.getAverageCost()),
                                new Money(item.getCurrency(), item.getMarketPrice())
                        ))
                        .toList(),
                new Watchlist(
                        entity.getUserId(),
                        entity.getWatchlistItems().stream()
                                .map(WatchlistItemEntity::getStockId)
                                .collect(Collectors.toSet())
                )
        );
    }
}
