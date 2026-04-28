package com.fintrade.core.stock.adapter.in.web;

import com.fintrade.core.stock.application.port.in.AddStockToIndexBasketUseCase;
import com.fintrade.core.stock.application.port.in.AssignStockClassificationUseCase;
import com.fintrade.core.stock.application.port.in.CreateIndexBasketUseCase;
import com.fintrade.core.stock.application.port.in.CreateStockClassificationUseCase;
import com.fintrade.core.stock.domain.model.ClassificationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/stocks")
public class AdminStockMasterController {

    private final CreateStockClassificationUseCase createStockClassificationUseCase;
    private final CreateIndexBasketUseCase createIndexBasketUseCase;
    private final AssignStockClassificationUseCase assignStockClassificationUseCase;
    private final AddStockToIndexBasketUseCase addStockToIndexBasketUseCase;

    public AdminStockMasterController(
            CreateStockClassificationUseCase createStockClassificationUseCase,
            CreateIndexBasketUseCase createIndexBasketUseCase,
            AssignStockClassificationUseCase assignStockClassificationUseCase,
            AddStockToIndexBasketUseCase addStockToIndexBasketUseCase
    ) {
        this.createStockClassificationUseCase = createStockClassificationUseCase;
        this.createIndexBasketUseCase = createIndexBasketUseCase;
        this.assignStockClassificationUseCase = assignStockClassificationUseCase;
        this.addStockToIndexBasketUseCase = addStockToIndexBasketUseCase;
    }

    @PostMapping("/classifications")
    public IdResponse createClassification(@Valid @RequestBody CreateClassificationRequest request) {
        return new IdResponse(createStockClassificationUseCase.handle(
                new CreateStockClassificationUseCase.CreateStockClassificationCommand(
                        request.code(),
                        request.name(),
                        request.type(),
                        request.description()
                )
        ));
    }

    @PostMapping("/index-baskets")
    public IdResponse createIndexBasket(@Valid @RequestBody CreateIndexBasketRequest request) {
        return new IdResponse(createIndexBasketUseCase.handle(
                new CreateIndexBasketUseCase.CreateIndexBasketCommand(
                        request.code(),
                        request.name(),
                        request.description()
                )
        ));
    }

    @PostMapping("/{stockId}/classifications/{classificationId}")
    public void assignClassification(@PathVariable UUID stockId, @PathVariable UUID classificationId) {
        assignStockClassificationUseCase.handle(new AssignStockClassificationUseCase.AssignStockClassificationCommand(stockId, classificationId));
    }

    @PostMapping("/index-baskets/{basketId}/members/{stockId}")
    public void addToIndexBasket(@PathVariable UUID basketId, @PathVariable UUID stockId) {
        addStockToIndexBasketUseCase.handle(new AddStockToIndexBasketUseCase.AddStockToIndexBasketCommand(basketId, stockId));
    }

    public record CreateClassificationRequest(
            @NotBlank String code,
            @NotBlank String name,
            @NotNull ClassificationType type,
            String description
    ) {
    }

    public record CreateIndexBasketRequest(
            @NotBlank String code,
            @NotBlank String name,
            String description
    ) {
    }

    public record IdResponse(UUID id) {
    }
}
