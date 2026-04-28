package com.fintrade.core.ledger.adapter.out.persistence.repository;

import com.fintrade.core.ledger.adapter.out.persistence.entity.SecuritiesLedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataSecuritiesLedgerEntryRepository extends JpaRepository<SecuritiesLedgerEntryEntity, UUID> {

    List<SecuritiesLedgerEntryEntity> findByUserId(UUID userId);
}
