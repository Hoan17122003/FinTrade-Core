package com.fintrade.core.ledger.adapter.out.persistence;

import com.fintrade.core.ledger.adapter.out.persistence.entity.FeeEntryEntity;
import com.fintrade.core.ledger.adapter.out.persistence.repository.SpringDataFeeEntryRepository;
import com.fintrade.core.ledger.application.port.out.FeeEntryRepository;
import com.fintrade.core.ledger.domain.model.FeeEntry;
import com.fintrade.core.shared.domain.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JpaFeeEntryRepositoryAdapter implements FeeEntryRepository {

    private final SpringDataFeeEntryRepository repository;

    public JpaFeeEntryRepositoryAdapter(SpringDataFeeEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public FeeEntry save(FeeEntry entry) {
        var entity = new FeeEntryEntity();
        entity.setId(entry.id());
        entity.setOrderId(entry.orderId());
        entity.setUserId(entry.userId());
        entity.setFeeCode(entry.feeCode());
        entity.setAmount(entry.amount().amount());
        entity.setCurrency(entry.amount().currency());
        entity.setCreatedAt(entry.createdAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public List<FeeEntry> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    private FeeEntry toDomain(FeeEntryEntity entity) {
        return new FeeEntry(
                entity.getId(),
                entity.getOrderId(),
                entity.getUserId(),
                entity.getFeeCode(),
                new Money(entity.getCurrency(), entity.getAmount()),
                entity.getCreatedAt()
        );
    }
}
