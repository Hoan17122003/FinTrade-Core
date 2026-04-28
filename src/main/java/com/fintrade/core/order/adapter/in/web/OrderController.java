package com.fintrade.core.order.adapter.in.web;

import com.fintrade.core.ledger.application.port.in.GetUserLedgerUseCase;
import com.fintrade.core.ledger.application.query.LedgerEntryView;
import com.fintrade.core.order.application.port.in.PlaceOrderUseCase;
import com.fintrade.core.order.application.port.in.SearchOrdersUseCase;
import com.fintrade.core.order.application.query.OrderView;
import com.fintrade.core.order.domain.model.OrderSide;
import com.fintrade.core.order.domain.model.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final SearchOrdersUseCase searchOrdersUseCase;
    private final GetUserLedgerUseCase getUserLedgerUseCase;

    public OrderController(
            PlaceOrderUseCase placeOrderUseCase,
            SearchOrdersUseCase searchOrdersUseCase,
            GetUserLedgerUseCase getUserLedgerUseCase
    ) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.searchOrdersUseCase = searchOrdersUseCase;
        this.getUserLedgerUseCase = getUserLedgerUseCase;
    }

    @PostMapping("/orders")
    public PlaceOrderUseCase.PlaceOrderResult placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return placeOrderUseCase.handle(new PlaceOrderUseCase.PlaceOrderCommand(
                request.userId(),
                request.stockId(),
                request.side(),
                request.quantity(),
                request.limitPrice()
        ));
    }

    @GetMapping("/orders")
    public List<OrderView> searchOrders(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID stockId,
            @RequestParam(required = false) OrderSide side,
            @RequestParam(required = false) OrderStatus status
    ) {
        return searchOrdersUseCase.handle(new SearchOrdersUseCase.OrderFilter(userId, stockId, side, status));
    }

    @GetMapping("/investors/{userId}/ledger")
    public List<LedgerEntryView> getUserLedger(@PathVariable UUID userId) {
        return getUserLedgerUseCase.handle(userId);
    }

    public record PlaceOrderRequest(
            @NotNull UUID userId,
            @NotNull UUID stockId,
            @NotNull OrderSide side,
            @NotNull @DecimalMin("0.0001") BigDecimal quantity,
            @NotNull @DecimalMin("0.01") BigDecimal limitPrice
    ) {
    }
}
