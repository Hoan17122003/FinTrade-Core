package com.fintrade.core.ledger.adapter.out.persistence.repository;

import com.fintrade.core.ledger.adapter.out.persistence.entity.FeeEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataFeeEntryRepository extends JpaRepository<FeeEntryEntity, UUID> {

    List<FeeEntryEntity> findByUserId(UUID userId);
}
