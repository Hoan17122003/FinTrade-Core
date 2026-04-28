package com.fintrade.core.portfolio.application.port.in;

import com.fintrade.core.portfolio.application.query.PortfolioView;

import java.util.UUID;

public interface GetPortfolioUseCase {

    PortfolioView handle(UUID userId);
}
