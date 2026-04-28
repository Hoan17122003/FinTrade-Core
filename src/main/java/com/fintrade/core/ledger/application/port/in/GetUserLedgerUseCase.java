package com.fintrade.core.ledger.application.port.in;

import com.fintrade.core.ledger.application.query.LedgerEntryView;

import java.util.List;
import java.util.UUID;

public interface GetUserLedgerUseCase {

    List<LedgerEntryView> handle(UUID userId);
}
