package com.fintrade.core.order.application.service;

import com.fintrade.core.ledger.application.port.in.GetUserLedgerUseCase;
import com.fintrade.core.ledger.application.port.out.CashLedgerEntryRepository;
import com.fintrade.core.ledger.application.port.out.FeeEntryRepository;
import com.fintrade.core.ledger.application.port.out.SecuritiesLedgerEntryRepository;
import com.fintrade.core.ledger.application.query.LedgerEntryView;
import com.fintrade.core.ledger.domain.model.CashLedgerEntry;
import com.fintrade.core.ledger.domain.model.CashLedgerEntryType;
import com.fintrade.core.ledger.domain.model.FeeEntry;
import com.fintrade.core.ledger.domain.model.SecuritiesLedgerEntry;
import com.fintrade.core.ledger.domain.model.SecuritiesLedgerEntryType;
import com.fintrade.core.order.application.port.in.PlaceOrderUseCase;
import com.fintrade.core.order.application.port.in.SearchOrdersUseCase;
import com.fintrade.core.order.application.port.out.OrderRepository;
import com.fintrade.core.order.application.query.OrderView;
import com.fintrade.core.order.domain.model.Order;
import com.fintrade.core.order.domain.model.OrderSide;
import com.fintrade.core.portfolio.application.port.out.PortfolioRepository;
import com.fintrade.core.portfolio.domain.model.Portfolio;
import com.fintrade.core.portfolio.domain.model.Position;
import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.shared.domain.Money;
import com.fintrade.core.stock.application.port.out.StockRepository;
import com.fintrade.core.stock.domain.model.TradingStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderApplicationService implements PlaceOrderUseCase, SearchOrdersUseCase, GetUserLedgerUseCase {

    private static final BigDecimal TRADING_FEE_RATE = new BigDecimal("0.001");

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final PortfolioRepository portfolioRepository;
    private final CashLedgerEntryRepository cashLedgerEntryRepository;
    private final SecuritiesLedgerEntryRepository securitiesLedgerEntryRepository;
    private final FeeEntryRepository feeEntryRepository;

    public OrderApplicationService(
            OrderRepository orderRepository,
            StockRepository stockRepository,
            PortfolioRepository portfolioRepository,
            CashLedgerEntryRepository cashLedgerEntryRepository,
            SecuritiesLedgerEntryRepository securitiesLedgerEntryRepository,
            FeeEntryRepository feeEntryRepository
    ) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.portfolioRepository = portfolioRepository;
        this.cashLedgerEntryRepository = cashLedgerEntryRepository;
        this.securitiesLedgerEntryRepository = securitiesLedgerEntryRepository;
        this.feeEntryRepository = feeEntryRepository;
    }

    @Override
    public PlaceOrderResult handle(PlaceOrderCommand command) {
        var stock = stockRepository.findById(command.stockId())
                .orElseThrow(() -> new DomainException("Stock not found"));
        if (stock.tradingStatus() != TradingStatus.TRADING) {
            throw new DomainException("Stock is not in trading status");
        }

        var order = Order.submit(
                command.userId(),
                command.stockId(),
                stock.ticker(),
                command.side(),
                command.quantity(),
                new Money(stock.currency(), command.limitPrice())
        ).accept().fill();

        var portfolio = portfolioRepository.findByUserId(command.userId())
                .orElse(Portfolio.empty(command.userId(), stock.currency()));
        var feeAmount = order.grossAmount().multiply(TRADING_FEE_RATE);
        ensureOrderCanBeExecuted(order, portfolio, feeAmount);

        var cashEntry = createCashLedgerEntry(order);
        var securitiesEntry = createSecuritiesLedgerEntry(order);
        var feeEntry = FeeEntry.create(order.id(), order.userId(), "TRADING_FEE", feeAmount);

        orderRepository.save(order);
        cashLedgerEntryRepository.save(cashEntry);
        securitiesLedgerEntryRepository.save(securitiesEntry);
        feeEntryRepository.save(feeEntry);
        portfolioRepository.save(applyToPortfolio(portfolio, order, feeAmount));

        return new PlaceOrderResult(order.id(), cashEntry.id(), securitiesEntry.id(), feeEntry.id());
    }

    @Override
    public List<OrderView> handle(OrderFilter filter) {
        return orderRepository.search(filter).stream()
                .map(order -> new OrderView(
                        order.id(),
                        order.userId(),
                        order.stockId(),
                        order.ticker(),
                        order.side(),
                        order.quantity(),
                        order.price().amount(),
                        order.price().currency(),
                        order.status(),
                        order.submittedAt(),
                        order.filledAt()
                ))
                .toList();
    }

    @Override
    public List<LedgerEntryView> handle(UUID userId) {
        var views = new ArrayList<LedgerEntryView>();

        cashLedgerEntryRepository.findByUserId(userId).forEach(entry ->
                views.add(new LedgerEntryView(
                        entry.id(),
                        "cash",
                        entry.entryType().name(),
                        entry.orderId(),
                        null,
                        null,
                        null,
                        null,
                        entry.amount().amount(),
                        entry.amount().currency(),
                        entry.createdAt()
                )));

        securitiesLedgerEntryRepository.findByUserId(userId).forEach(entry ->
                views.add(new LedgerEntryView(
                        entry.id(),
                        "securities",
                        entry.entryType().name(),
                        entry.orderId(),
                        entry.stockId(),
                        entry.ticker(),
                        null,
                        entry.quantity(),
                        null,
                        null,
                        entry.createdAt()
                )));

        feeEntryRepository.findByUserId(userId).forEach(entry ->
                views.add(new LedgerEntryView(
                        entry.id(),
                        "fee",
                        "FEE",
                        entry.orderId(),
                        null,
                        null,
                        entry.feeCode(),
                        null,
                        entry.amount().amount(),
                        entry.amount().currency(),
                        entry.createdAt()
                )));

        return views.stream()
                .sorted(Comparator.comparing(LedgerEntryView::createdAt))
                .toList();
    }

    private void ensureOrderCanBeExecuted(Order order, Portfolio portfolio, Money feeAmount) {
        if (order.side() == OrderSide.BUY) {
            var totalRequired = order.grossAmount().add(feeAmount);
            if (portfolio.cashBalance().amount().compareTo(totalRequired.amount()) < 0) {
                throw new DomainException("Insufficient cash balance");
            }
            return;
        }

        var position = portfolio.positions().stream()
                .filter(item -> item.stockId().equals(order.stockId()))
                .findFirst()
                .orElseThrow(() -> new DomainException("Position not found"));
        if (position.quantity().compareTo(order.quantity()) < 0) {
            throw new DomainException("Insufficient holdings quantity");
        }
    }

    private CashLedgerEntry createCashLedgerEntry(Order order) {
        return CashLedgerEntry.create(
                order.id(),
                order.userId(),
                order.side() == OrderSide.BUY ? CashLedgerEntryType.TRADE_BUY_DEBIT : CashLedgerEntryType.TRADE_SELL_CREDIT,
                order.grossAmount()
        );
    }

    private SecuritiesLedgerEntry createSecuritiesLedgerEntry(Order order) {
        return SecuritiesLedgerEntry.create(
                order.id(),
                order.userId(),
                order.stockId(),
                order.ticker(),
                order.side() == OrderSide.BUY ? SecuritiesLedgerEntryType.BUY_CREDIT : SecuritiesLedgerEntryType.SELL_DEBIT,
                order.quantity()
        );
    }

    private Portfolio applyToPortfolio(Portfolio portfolio, Order order, Money feeAmount) {
        var positions = new ArrayList<>(portfolio.positions());
        var existingPosition = positions.stream()
                .filter(item -> item.stockId().equals(order.stockId()))
                .findFirst()
                .orElse(null);
        if (existingPosition != null) {
            positions.remove(existingPosition);
        }

        Money updatedCashBalance;

        if (order.side() == OrderSide.BUY) {
            updatedCashBalance = portfolio.cashBalance().subtract(order.grossAmount()).subtract(feeAmount);
            var newPosition = mergeBuyPosition(existingPosition, order);
            positions.add(newPosition);
        } else {
            updatedCashBalance = portfolio.cashBalance().add(order.grossAmount()).subtract(feeAmount);
            var updated = reduceSellPosition(existingPosition, order);
            if (updated != null) {
                positions.add(updated);
            }
        }

        return new Portfolio(portfolio.userId(), updatedCashBalance, positions, portfolio.watchlist());
    }

    private Position mergeBuyPosition(Position existingPosition, Order order) {
        if (existingPosition == null) {
            return new Position(order.stockId(), order.ticker(), order.quantity(), order.price(), order.price());
        }

        var existingCost = existingPosition.averageCost().amount().multiply(existingPosition.quantity());
        var newCost = order.price().amount().multiply(order.quantity());
        var totalQuantity = existingPosition.quantity().add(order.quantity());
        var weightedAverage = existingCost.add(newCost).divide(totalQuantity, 2, RoundingMode.HALF_UP);

        return new Position(
                order.stockId(),
                order.ticker(),
                totalQuantity,
                new Money(order.price().currency(), weightedAverage),
                order.price()
        );
    }

    private Position reduceSellPosition(Position existingPosition, Order order) {
        if (existingPosition == null) {
            throw new DomainException("Position not found");
        }
        var remainingQuantity = existingPosition.quantity().subtract(order.quantity());
        if (remainingQuantity.signum() == 0) {
            return null;
        }
        return new Position(
                existingPosition.stockId(),
                existingPosition.ticker(),
                remainingQuantity,
                existingPosition.averageCost(),
                order.price()
        );
    }
}
