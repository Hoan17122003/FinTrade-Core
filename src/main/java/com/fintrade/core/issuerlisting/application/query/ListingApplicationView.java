package com.fintrade.core.issuerlisting.application.query;

import com.fintrade.core.issuerlisting.domain.model.ListingApplicationStatus;

import java.time.Instant;
import java.util.UUID;

public record ListingApplicationView(
        UUID id,
        UUID issuerCompanyId,
        String issuerCompanyName,
        String ticker,
        String displayName,
        ListingApplicationStatus status,
        boolean criteriaSatisfied,
        String criteriaSummary,
        Instant submittedAt,
        Instant reviewedAt
) {
}
