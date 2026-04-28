package com.fintrade.core.ledger.application.port.out;

import com.fintrade.core.ledger.domain.model.FeeEntry;

import java.util.List;
import java.util.UUID;

public interface FeeEntryRepository {

    FeeEntry save(FeeEntry entry);

    List<FeeEntry> findByUserId(UUID userId);
}
