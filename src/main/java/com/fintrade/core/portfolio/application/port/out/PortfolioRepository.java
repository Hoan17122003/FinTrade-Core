package com.fintrade.core.portfolio.application.port.out;

import com.fintrade.core.portfolio.domain.model.Portfolio;

import java.util.Optional;
import java.util.UUID;

public interface PortfolioRepository {

    Portfolio save(Portfolio portfolio);

    Optional<Portfolio> findByUserId(UUID userId);
}
