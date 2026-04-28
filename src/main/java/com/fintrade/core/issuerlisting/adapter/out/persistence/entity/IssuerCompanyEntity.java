package com.fintrade.core.issuerlisting.adapter.out.persistence.entity;

import com.fintrade.core.issuerlisting.domain.model.IssuerCompanyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "issuer_company")
public class IssuerCompanyEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String legalName;

    @Column(nullable = false, unique = true)
    private String businessRegistrationNumber;

    @Column(nullable = false)
    private String primarySector;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuerCompanyStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    public IssuerCompanyEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public String getPrimarySector() {
        return primarySector;
    }

    public void setPrimarySector(String primarySector) {
        this.primarySector = primarySector;
    }

    public IssuerCompanyStatus getStatus() {
        return status;
    }

    public void setStatus(IssuerCompanyStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
