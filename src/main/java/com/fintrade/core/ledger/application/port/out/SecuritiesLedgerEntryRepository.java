package com.fintrade.core.ledger.application.port.out;

import com.fintrade.core.ledger.domain.model.SecuritiesLedgerEntry;

import java.util.List;
import java.util.UUID;

public interface SecuritiesLedgerEntryRepository {

    SecuritiesLedgerEntry save(SecuritiesLedgerEntry entry);

    List<SecuritiesLedgerEntry> findByUserId(UUID userId);
}
