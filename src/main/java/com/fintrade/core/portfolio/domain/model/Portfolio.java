package com.fintrade.core.portfolio.domain.model;

import com.fintrade.core.shared.domain.Money;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Portfolio(
        UUID userId,
        Money cashBalance,
        List<Position> positions,
        Watchlist watchlist
) {

    public Portfolio {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(cashBalance, "cashBalance must not be null");
        positions = List.copyOf(positions);
        Objects.requireNonNull(watchlist, "watchlist must not be null");
    }

    public static Portfolio empty(UUID userId, String currency) {
        return new Portfolio(userId, Money.zero(currency), List.of(), Watchlist.empty(userId));
    }
}
