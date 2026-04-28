package com.fintrade.core.ledger.adapter.out.persistence;

import com.fintrade.core.ledger.adapter.out.persistence.entity.CashLedgerEntryEntity;
import com.fintrade.core.ledger.adapter.out.persistence.repository.SpringDataCashLedgerEntryRepository;
import com.fintrade.core.ledger.application.port.out.CashLedgerEntryRepository;
import com.fintrade.core.ledger.domain.model.CashLedgerEntry;
import com.fintrade.core.shared.domain.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JpaCashLedgerEntryRepositoryAdapter implements CashLedgerEntryRepository {

    private final SpringDataCashLedgerEntryRepository repository;

    public JpaCashLedgerEntryRepositoryAdapter(SpringDataCashLedgerEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CashLedgerEntry save(CashLedgerEntry entry) {
        var entity = new CashLedgerEntryEntity();
        entity.setId(entry.id());
        entity.setOrderId(entry.orderId());
        entity.setUserId(entry.userId());
        entity.setEntryType(entry.entryType());
        entity.setAmount(entry.amount().amount());
        entity.setCurrency(entry.amount().currency());
        entity.setCreatedAt(entry.createdAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public List<CashLedgerEntry> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    private CashLedgerEntry toDomain(CashLedgerEntryEntity entity) {
        return new CashLedgerEntry(
                entity.getId(),
                entity.getOrderId(),
                entity.getUserId(),
                entity.getEntryType(),
                new Money(entity.getCurrency(), entity.getAmount()),
                entity.getCreatedAt()
        );
    }
}
