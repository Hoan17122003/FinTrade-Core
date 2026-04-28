package com.fintrade.core.issuerlisting.domain.model;

import com.fintrade.core.shared.domain.DomainException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record IssuerCompany(
        UUID id,
        String legalName,
        String businessRegistrationNumber,
        String primarySector,
        IssuerCompanyStatus status,
        Instant createdAt
) {

    public IssuerCompany {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(legalName, "legalName must not be null");
        Objects.requireNonNull(businessRegistrationNumber, "businessRegistrationNumber must not be null");
        Objects.requireNonNull(primarySector, "primarySector must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        if (legalName.isBlank() || businessRegistrationNumber.isBlank() || primarySector.isBlank()) {
            throw new DomainException("Issuer company fields must not be blank");
        }
    }

    public static IssuerCompany register(String legalName, String businessRegistrationNumber, String primarySector) {
        return new IssuerCompany(
                UUID.randomUUID(),
                legalName,
                businessRegistrationNumber,
                primarySector,
                IssuerCompanyStatus.PENDING_REVIEW,
                Instant.now()
        );
    }

    public IssuerCompany markEligible() {
        return new IssuerCompany(id, legalName, businessRegistrationNumber, primarySector, IssuerCompanyStatus.ELIGIBLE, createdAt);
    }

    public IssuerCompany markListed() {
        if (status != IssuerCompanyStatus.ELIGIBLE) {
            throw new DomainException("Issuer company must be eligible before listing");
        }
        return new IssuerCompany(id, legalName, businessRegistrationNumber, primarySector, IssuerCompanyStatus.LISTED, createdAt);
    }
}
