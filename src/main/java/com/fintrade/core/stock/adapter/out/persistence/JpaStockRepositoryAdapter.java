package com.fintrade.core.stock.adapter.out.persistence;

import com.fintrade.core.issuerlisting.adapter.out.persistence.repository.SpringDataIssuerCompanyRepository;
import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.stock.adapter.out.persistence.entity.StockEntity;
import com.fintrade.core.stock.adapter.out.persistence.repository.SpringDataIndexBasketRepository;
import com.fintrade.core.stock.adapter.out.persistence.repository.SpringDataStockClassificationRepository;
import com.fintrade.core.stock.adapter.out.persistence.repository.SpringDataStockRepository;
import com.fintrade.core.stock.adapter.out.persistence.specification.StockSpecifications;
import com.fintrade.core.stock.application.port.in.SearchStocksUseCase.StockFilter;
import com.fintrade.core.stock.application.port.out.StockRepository;
import com.fintrade.core.stock.domain.model.Stock;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaStockRepositoryAdapter implements StockRepository {

    private final SpringDataStockRepository repository;
    private final SpringDataIssuerCompanyRepository issuerCompanyRepository;
    private final SpringDataStockClassificationRepository stockClassificationRepository;
    private final SpringDataIndexBasketRepository indexBasketRepository;

    public JpaStockRepositoryAdapter(
            SpringDataStockRepository repository,
            SpringDataIssuerCompanyRepository issuerCompanyRepository,
            SpringDataStockClassificationRepository stockClassificationRepository,
            SpringDataIndexBasketRepository indexBasketRepository
    ) {
        this.repository = repository;
        this.issuerCompanyRepository = issuerCompanyRepository;
        this.stockClassificationRepository = stockClassificationRepository;
        this.indexBasketRepository = indexBasketRepository;
    }

    @Override
    public Stock save(Stock stock) {
        var entity = repository.findById(stock.id()).orElseGet(StockEntity::new);
        entity.setId(stock.id());
        entity.setTicker(stock.ticker());
        entity.setDisplayName(stock.displayName());
        entity.setIssuerCompany(issuerCompanyRepository.findById(stock.issuerCompanyId())
                .orElseThrow(() -> new DomainException("Issuer company not found")));
        entity.setParValue(stock.parValue());
        entity.setLotSize(stock.lotSize());
        entity.setCurrency(stock.currency());
        entity.setMarket(stock.market());
        entity.setListingStatus(stock.listingStatus());
        entity.setTradingStatus(stock.tradingStatus());
        entity.setListingDate(stock.listingDate());
        entity.setClassifications(new LinkedHashSet<>(stockClassificationRepository.findAllById(stock.classificationIds())));
        entity.setIndexBaskets(new LinkedHashSet<>(indexBasketRepository.findAllById(stock.indexBasketIds())));
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Stock> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Stock> search(StockFilter filter) {
        return repository.findAll(StockSpecifications.withFilter(filter)).stream()
                .map(this::toDomain)
                .toList();
    }

    private Stock toDomain(StockEntity entity) {
        return new Stock(
                entity.getId(),
                entity.getTicker(),
                entity.getDisplayName(),
                entity.getIssuerCompany().getId(),
                entity.getParValue(),
                entity.getLotSize(),
                entity.getCurrency(),
                entity.getMarket(),
                entity.getListingStatus(),
                entity.getTradingStatus(),
                entity.getListingDate(),
                entity.getClassifications().stream().map(com.fintrade.core.stock.adapter.out.persistence.entity.StockClassificationEntity::getId).collect(java.util.stream.Collectors.toSet()),
                entity.getIndexBaskets().stream().map(com.fintrade.core.stock.adapter.out.persistence.entity.IndexBasketEntity::getId).collect(java.util.stream.Collectors.toSet())
        );
    }
}
