package com.fintrade.core.stock.application.service;

import com.fintrade.core.issuerlisting.application.port.out.IssuerCompanyRepository;
import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.stock.application.port.in.AddStockToIndexBasketUseCase;
import com.fintrade.core.stock.application.port.in.AssignStockClassificationUseCase;
import com.fintrade.core.stock.application.port.in.CreateIndexBasketUseCase;
import com.fintrade.core.stock.application.port.in.CreateStockClassificationUseCase;
import com.fintrade.core.stock.application.port.in.GetStockDetailUseCase;
import com.fintrade.core.stock.application.port.in.SearchStocksUseCase;
import com.fintrade.core.stock.application.port.out.IndexBasketRepository;
import com.fintrade.core.stock.application.port.out.StockClassificationRepository;
import com.fintrade.core.stock.application.port.out.StockRepository;
import com.fintrade.core.stock.application.query.StockDetailView;
import com.fintrade.core.stock.domain.model.IndexBasket;
import com.fintrade.core.stock.domain.model.StockClassification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StockMasterApplicationService implements
        CreateStockClassificationUseCase,
        CreateIndexBasketUseCase,
        AssignStockClassificationUseCase,
        AddStockToIndexBasketUseCase,
        SearchStocksUseCase,
        GetStockDetailUseCase {

    private final StockRepository stockRepository;
    private final StockClassificationRepository stockClassificationRepository;
    private final IndexBasketRepository indexBasketRepository;
    private final IssuerCompanyRepository issuerCompanyRepository;

    public StockMasterApplicationService(
            StockRepository stockRepository,
            StockClassificationRepository stockClassificationRepository,
            IndexBasketRepository indexBasketRepository,
            IssuerCompanyRepository issuerCompanyRepository
    ) {
        this.stockRepository = stockRepository;
        this.stockClassificationRepository = stockClassificationRepository;
        this.indexBasketRepository = indexBasketRepository;
        this.issuerCompanyRepository = issuerCompanyRepository;
    }

    @Override
    public UUID handle(CreateStockClassificationCommand command) {
        var classification = StockClassification.create(
                command.code(),
                command.name(),
                command.type(),
                command.description()
        );
        return stockClassificationRepository.save(classification).id();
    }

    @Override
    public UUID handle(CreateIndexBasketCommand command) {
        var indexBasket = IndexBasket.create(command.code(), command.name(), command.description());
        return indexBasketRepository.save(indexBasket).id();
    }

    @Override
    public void handle(AssignStockClassificationCommand command) {
        var stock = stockRepository.findById(command.stockId())
                .orElseThrow(() -> new DomainException("Stock not found"));
        stockClassificationRepository.findById(command.classificationId())
                .orElseThrow(() -> new DomainException("Classification not found"));
        stockRepository.save(stock.addClassification(command.classificationId()));
    }

    @Override
    public void handle(AddStockToIndexBasketCommand command) {
        var stock = stockRepository.findById(command.stockId())
                .orElseThrow(() -> new DomainException("Stock not found"));
        var basket = indexBasketRepository.findById(command.basketId())
                .orElseThrow(() -> new DomainException("Index basket not found"));
        stockRepository.save(stock.addIndexBasket(command.basketId()));
        indexBasketRepository.save(basket.addStock(command.stockId()));
    }

    @Override
    public List<StockDetailView> handle(StockFilter filter) {
        return stockRepository.search(filter).stream()
                .map(this::toView)
                .toList();
    }

    @Override
    public StockDetailView handle(UUID stockId) {
        var stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new DomainException("Stock not found"));
        return toView(stock);
    }

    private StockDetailView toView(com.fintrade.core.stock.domain.model.Stock stock) {
        var issuer = issuerCompanyRepository.findById(stock.issuerCompanyId())
                .orElseThrow(() -> new DomainException("Issuer company not found"));
        var classifications = stockClassificationRepository.findByIds(stock.classificationIds())
                .stream()
                .map(item -> item.code() + ":" + item.name())
                .toList();
        var indexBaskets = indexBasketRepository.findByIds(stock.indexBasketIds())
                .stream()
                .map(item -> item.code() + ":" + item.name())
                .toList();

        return new StockDetailView(
                stock.id(),
                stock.ticker(),
                stock.displayName(),
                issuer.legalName(),
                issuer.primarySector(),
                stock.market(),
                stock.listingStatus(),
                stock.tradingStatus(),
                stock.listingDate(),
                classifications,
                indexBaskets
        );
    }
}
