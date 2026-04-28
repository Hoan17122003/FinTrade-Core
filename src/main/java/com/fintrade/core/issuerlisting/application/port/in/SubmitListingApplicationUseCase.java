package com.fintrade.core.issuerlisting.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface SubmitListingApplicationUseCase {

    UUID handle(SubmitListingApplicationCommand command);

    record SubmitListingApplicationCommand(
            UUID issuerCompanyId,
            String ticker,
            String displayName,
            BigDecimal parValue,
            int lotSize,
            String currency,
            String market,
            boolean criteriaSatisfied,
            String criteriaSummary,
            String submittedBy
    ) {
    }
}
