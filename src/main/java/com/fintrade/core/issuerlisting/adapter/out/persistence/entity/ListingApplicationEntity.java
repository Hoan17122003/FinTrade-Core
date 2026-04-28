package com.fintrade.core.issuerlisting.adapter.out.persistence.entity;

import com.fintrade.core.issuerlisting.domain.model.ListingApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "listing_application")
public class ListingApplicationEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issuer_company_id", nullable = false)
    private IssuerCompanyEntity issuerCompany;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal parValue;

    @Column(nullable = false)
    private int lotSize;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false)
    private String market;

    @Column(nullable = false)
    private boolean criteriaSatisfied;

    @Column(nullable = false, length = 2000)
    private String criteriaSummary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingApplicationStatus status;

    @Column(nullable = false)
    private String submittedBy;

    private String reviewedBy;

    @Column(length = 1000)
    private String rejectionReason;

    @Column(nullable = false)
    private Instant submittedAt;

    private Instant reviewedAt;

    public ListingApplicationEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IssuerCompanyEntity getIssuerCompany() {
        return issuerCompany;
    }

    public void setIssuerCompany(IssuerCompanyEntity issuerCompany) {
        this.issuerCompany = issuerCompany;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public boolean isCriteriaSatisfied() {
        return criteriaSatisfied;
    }

    public void setCriteriaSatisfied(boolean criteriaSatisfied) {
        this.criteriaSatisfied = criteriaSatisfied;
    }

    public String getCriteriaSummary() {
        return criteriaSummary;
    }

    public void setCriteriaSummary(String criteriaSummary) {
        this.criteriaSummary = criteriaSummary;
    }

    public ListingApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ListingApplicationStatus status) {
        this.status = status;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
