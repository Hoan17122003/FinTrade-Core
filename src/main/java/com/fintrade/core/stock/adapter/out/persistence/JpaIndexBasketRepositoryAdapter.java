package com.fintrade.core.stock.adapter.out.persistence;

import com.fintrade.core.stock.adapter.out.persistence.entity.IndexBasketEntity;
import com.fintrade.core.stock.adapter.out.persistence.repository.SpringDataIndexBasketRepository;
import com.fintrade.core.stock.application.port.out.IndexBasketRepository;
import com.fintrade.core.stock.domain.model.IndexBasket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class JpaIndexBasketRepositoryAdapter implements IndexBasketRepository {

    private final SpringDataIndexBasketRepository repository;

    public JpaIndexBasketRepositoryAdapter(SpringDataIndexBasketRepository repository) {
        this.repository = repository;
    }

    @Override
    public IndexBasket save(IndexBasket indexBasket) {
        var entity = repository.findById(indexBasket.id()).orElseGet(IndexBasketEntity::new);
        entity.setId(indexBasket.id());
        entity.setCode(indexBasket.code());
        entity.setName(indexBasket.name());
        entity.setDescription(indexBasket.description());
        repository.save(entity);
        return indexBasket;
    }

    @Override
    public Optional<IndexBasket> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<IndexBasket> findByIds(Set<UUID> ids) {
        return repository.findAllById(ids).stream().map(this::toDomain).toList();
    }

    private IndexBasket toDomain(IndexBasketEntity entity) {
        return new IndexBasket(entity.getId(), entity.getCode(), entity.getName(), entity.getDescription(), Set.of());
    }
}
