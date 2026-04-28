package com.fintrade.core.ledger.adapter.out.persistence;

import com.fintrade.core.ledger.adapter.out.persistence.entity.SecuritiesLedgerEntryEntity;
import com.fintrade.core.ledger.adapter.out.persistence.repository.SpringDataSecuritiesLedgerEntryRepository;
import com.fintrade.core.ledger.application.port.out.SecuritiesLedgerEntryRepository;
import com.fintrade.core.ledger.domain.model.SecuritiesLedgerEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JpaSecuritiesLedgerEntryRepositoryAdapter implements SecuritiesLedgerEntryRepository {

    private final SpringDataSecuritiesLedgerEntryRepository repository;

    public JpaSecuritiesLedgerEntryRepositoryAdapter(SpringDataSecuritiesLedgerEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public SecuritiesLedgerEntry save(SecuritiesLedgerEntry entry) {
        var entity = new SecuritiesLedgerEntryEntity();
        entity.setId(entry.id());
        entity.setOrderId(entry.orderId());
        entity.setUserId(entry.userId());
        entity.setStockId(entry.stockId());
        entity.setTicker(entry.ticker());
        entity.setEntryType(entry.entryType());
        entity.setQuantity(entry.quantity());
        entity.setCreatedAt(entry.createdAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public List<SecuritiesLedgerEntry> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    private SecuritiesLedgerEntry toDomain(SecuritiesLedgerEntryEntity entity) {
        return new SecuritiesLedgerEntry(
                entity.getId(),
                entity.getOrderId(),
                entity.getUserId(),
                entity.getStockId(),
                entity.getTicker(),
                entity.getEntryType(),
                entity.getQuantity(),
                entity.getCreatedAt()
        );
    }
}
