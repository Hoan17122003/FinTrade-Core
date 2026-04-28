package com.fintrade.core.stock.adapter.out.persistence;

import com.fintrade.core.stock.adapter.out.persistence.entity.StockClassificationEntity;
import com.fintrade.core.stock.adapter.out.persistence.repository.SpringDataStockClassificationRepository;
import com.fintrade.core.stock.application.port.out.StockClassificationRepository;
import com.fintrade.core.stock.domain.model.StockClassification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class JpaStockClassificationRepositoryAdapter implements StockClassificationRepository {

    private final SpringDataStockClassificationRepository repository;

    public JpaStockClassificationRepositoryAdapter(SpringDataStockClassificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public StockClassification save(StockClassification stockClassification) {
        var entity = repository.findById(stockClassification.id()).orElseGet(StockClassificationEntity::new);
        entity.setId(stockClassification.id());
        entity.setCode(stockClassification.code());
        entity.setName(stockClassification.name());
        entity.setType(stockClassification.type());
        entity.setDescription(stockClassification.description());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<StockClassification> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<StockClassification> findByIds(Set<UUID> ids) {
        return repository.findAllById(ids).stream().map(this::toDomain).toList();
    }

    private StockClassification toDomain(StockClassificationEntity entity) {
        return new StockClassification(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getDescription()
        );
    }
}
