package com.fintrade.core.stock.adapter.out.persistence.entity;

import com.fintrade.core.issuerlisting.adapter.out.persistence.entity.IssuerCompanyEntity;
import com.fintrade.core.stock.domain.model.StockLifecycleStatus;
import com.fintrade.core.stock.domain.model.TradingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stock")
public class StockEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @Column(nullable = false)
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issuer_company_id", nullable = false)
    private IssuerCompanyEntity issuerCompany;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal parValue;

    @Column(nullable = false)
    private int lotSize;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false)
    private String market;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockLifecycleStatus listingStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradingStatus tradingStatus;

    @Column(nullable = false)
    private LocalDate listingDate;

    @ManyToMany
    @JoinTable(
            name = "stock_classification_link",
            joinColumns = @JoinColumn(name = "stock_id"),
            inverseJoinColumns = @JoinColumn(name = "classification_id")
    )
    private Set<StockClassificationEntity> classifications = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "stock_index_basket_link",
            joinColumns = @JoinColumn(name = "stock_id"),
            inverseJoinColumns = @JoinColumn(name = "index_basket_id")
    )
    private Set<IndexBasketEntity> indexBaskets = new LinkedHashSet<>();

    public StockEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public IssuerCompanyEntity getIssuerCompany() {
        return issuerCompany;
    }

    public void setIssuerCompany(IssuerCompanyEntity issuerCompany) {
        this.issuerCompany = issuerCompany;
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

    public StockLifecycleStatus getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(StockLifecycleStatus listingStatus) {
        this.listingStatus = listingStatus;
    }

    public TradingStatus getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(TradingStatus tradingStatus) {
        this.tradingStatus = tradingStatus;
    }

    public LocalDate getListingDate() {
        return listingDate;
    }

    public void setListingDate(LocalDate listingDate) {
        this.listingDate = listingDate;
    }

    public Set<StockClassificationEntity> getClassifications() {
        return classifications;
    }

    public void setClassifications(Set<StockClassificationEntity> classifications) {
        this.classifications = classifications;
    }

    public Set<IndexBasketEntity> getIndexBaskets() {
        return indexBaskets;
    }

    public void setIndexBaskets(Set<IndexBasketEntity> indexBaskets) {
        this.indexBaskets = indexBaskets;
    }
}
