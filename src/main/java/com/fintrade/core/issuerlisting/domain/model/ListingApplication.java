package com.fintrade.core.issuerlisting.domain.model;

import com.fintrade.core.shared.domain.DomainException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record ListingApplication(
        UUID id,
        UUID issuerCompanyId,
        String ticker,
        String displayName,
        BigDecimal parValue,
        int lotSize,
        String currency,
        String market,
        boolean criteriaSatisfied,
        String criteriaSummary,
        ListingApplicationStatus status,
        String submittedBy,
        String reviewedBy,
        String rejectionReason,
        Instant submittedAt,
        Instant reviewedAt
) {

    public ListingApplication {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(issuerCompanyId, "issuerCompanyId must not be null");
        Objects.requireNonNull(ticker, "ticker must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(parValue, "parValue must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        Objects.requireNonNull(market, "market must not be null");
        Objects.requireNonNull(criteriaSummary, "criteriaSummary must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(submittedBy, "submittedBy must not be null");
        Objects.requireNonNull(submittedAt, "submittedAt must not be null");
        if (ticker.isBlank() || displayName.isBlank() || currency.isBlank() || market.isBlank()) {
            throw new DomainException("Listing application fields must not be blank");
        }
        if (parValue.signum() <= 0) {
            throw new DomainException("Par value must be positive");
        }
        if (lotSize <= 0) {
            throw new DomainException("Lot size must be positive");
        }
    }

    public static ListingApplication submit(
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
        return new ListingApplication(
                UUID.randomUUID(),
                issuerCompanyId,
                ticker,
                displayName,
                parValue,
                lotSize,
                currency,
                market,
                criteriaSatisfied,
                criteriaSummary,
                ListingApplicationStatus.SUBMITTED,
                submittedBy,
                null,
                null,
                Instant.now(),
                null
        );
    }

    public ListingApplication approve(String reviewer) {
        if (!criteriaSatisfied) {
            throw new DomainException("Listing criteria are not satisfied");
        }
        if (status != ListingApplicationStatus.SUBMITTED) {
            throw new DomainException("Only submitted applications can be approved");
        }
        return new ListingApplication(
                id,
                issuerCompanyId,
                ticker,
                displayName,
                parValue,
                lotSize,
                currency,
                market,
                criteriaSatisfied,
                criteriaSummary,
                ListingApplicationStatus.APPROVED,
                submittedBy,
                reviewer,
                null,
                submittedAt,
                Instant.now()
        );
    }

    public ListingApplication activate() {
        if (status != ListingApplicationStatus.APPROVED) {
            throw new DomainException("Only approved applications can be activated");
        }
        return new ListingApplication(
                id,
                issuerCompanyId,
                ticker,
                displayName,
                parValue,
                lotSize,
                currency,
                market,
                criteriaSatisfied,
                criteriaSummary,
                ListingApplicationStatus.ACTIVATED,
                submittedBy,
                reviewedBy,
                rejectionReason,
                submittedAt,
                reviewedAt
        );
    }
}
