package com.fintrade.core;

import com.fintrade.core.issuerlisting.application.port.in.ApproveListingApplicationUseCase;
import com.fintrade.core.issuerlisting.application.port.in.CreateIssuerCompanyUseCase;
import com.fintrade.core.issuerlisting.application.port.in.SubmitListingApplicationUseCase;
import com.fintrade.core.order.application.port.in.PlaceOrderUseCase;
import com.fintrade.core.order.application.port.in.SearchOrdersUseCase;
import com.fintrade.core.order.domain.model.OrderSide;
import com.fintrade.core.portfolio.application.port.in.GetPortfolioUseCase;
import com.fintrade.core.portfolio.application.port.in.UpsertPortfolioCashUseCase;
import com.fintrade.core.ledger.application.port.in.GetUserLedgerUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TradingFlowIntegrationTest {

    @Autowired
    private CreateIssuerCompanyUseCase createIssuerCompanyUseCase;

    @Autowired
    private SubmitListingApplicationUseCase submitListingApplicationUseCase;

    @Autowired
    private ApproveListingApplicationUseCase approveListingApplicationUseCase;

    @Autowired
    private UpsertPortfolioCashUseCase upsertPortfolioCashUseCase;

    @Autowired
    private PlaceOrderUseCase placeOrderUseCase;

    @Autowired
    private GetPortfolioUseCase getPortfolioUseCase;

    @Autowired
    private GetUserLedgerUseCase getUserLedgerUseCase;

    @Autowired
    private SearchOrdersUseCase searchOrdersUseCase;

    @Test
    void shouldPlaceBuyOrderAndUpdatePortfolioAndLedger() {
        var issuerId = createIssuerCompanyUseCase.handle(
                new CreateIssuerCompanyUseCase.CreateIssuerCompanyCommand("FPT Corporation", "BRN-FPT-001", "Technology")
        );

        var listingId = submitListingApplicationUseCase.handle(
                new SubmitListingApplicationUseCase.SubmitListingApplicationCommand(
                        issuerId,
                        "FPT",
                        "FPT Corp",
                        new BigDecimal("10000"),
                        100,
                        "VND",
                        "HOSE",
                        true,
                        "All listing criteria satisfied",
                        "admin"
                )
        );

        var stockId = approveListingApplicationUseCase.handle(
                new ApproveListingApplicationUseCase.ApproveListingApplicationCommand(listingId, "reviewer")
        );

        var userId = UUID.randomUUID();
        upsertPortfolioCashUseCase.handle(
                new UpsertPortfolioCashUseCase.UpsertPortfolioCashCommand(userId, "VND", new BigDecimal("1000000"))
        );

        var result = placeOrderUseCase.handle(
                new PlaceOrderUseCase.PlaceOrderCommand(userId, stockId, OrderSide.BUY, new BigDecimal("10"), new BigDecimal("10000"))
        );

        var portfolio = getPortfolioUseCase.handle(userId);
        var ledgerEntries = getUserLedgerUseCase.handle(userId);
        var orders = searchOrdersUseCase.handle(new SearchOrdersUseCase.OrderFilter(userId, stockId, OrderSide.BUY, null));

        assertThat(result.orderId()).isNotNull();
        assertThat(portfolio.cashBalance()).isEqualByComparingTo("899900.00");
        assertThat(portfolio.stockValue()).isEqualByComparingTo("100000.00");
        assertThat(portfolio.totalValue()).isEqualByComparingTo("999900.00");
        assertThat(portfolio.holdings()).hasSize(1);
        assertThat(portfolio.holdings().getFirst().ticker()).isEqualTo("FPT");
        assertThat(portfolio.holdings().getFirst().quantity()).isEqualByComparingTo("10.0000");
        assertThat(ledgerEntries).hasSize(3);
        assertThat(orders).hasSize(1);
        assertThat(orders.getFirst().ticker()).isEqualTo("FPT");
    }
}
