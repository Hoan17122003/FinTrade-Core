package com.fintrade.core.portfolio.domain.model;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Watchlist(UUID userId, Set<UUID> stockIds) {

    public Watchlist {
        Objects.requireNonNull(userId, "userId must not be null");
        stockIds = Set.copyOf(stockIds);
    }

    public static Watchlist empty(UUID userId) {
        return new Watchlist(userId, Set.of());
    }

    public Watchlist addStock(UUID stockId) {
        var updated = new LinkedHashSet<>(stockIds);
        updated.add(stockId);
        return new Watchlist(userId, updated);
    }
}
