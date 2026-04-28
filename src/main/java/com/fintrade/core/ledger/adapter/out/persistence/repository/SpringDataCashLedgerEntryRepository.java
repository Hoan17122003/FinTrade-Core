package com.fintrade.core.ledger.adapter.out.persistence.repository;

import com.fintrade.core.ledger.adapter.out.persistence.entity.CashLedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCashLedgerEntryRepository extends JpaRepository<CashLedgerEntryEntity, UUID> {

    List<CashLedgerEntryEntity> findByUserId(UUID userId);
}
