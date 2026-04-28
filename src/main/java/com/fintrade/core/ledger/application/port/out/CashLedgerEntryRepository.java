package com.fintrade.core.ledger.application.port.out;

import com.fintrade.core.ledger.domain.model.CashLedgerEntry;

import java.util.List;
import java.util.UUID;

public interface CashLedgerEntryRepository {

    CashLedgerEntry save(CashLedgerEntry entry);

    List<CashLedgerEntry> findByUserId(UUID userId);
}
