package com.fintrade.core.issuerlisting.adapter.out.persistence;

import com.fintrade.core.issuerlisting.adapter.out.persistence.entity.ListingApplicationEntity;
import com.fintrade.core.issuerlisting.adapter.out.persistence.repository.SpringDataIssuerCompanyRepository;
import com.fintrade.core.issuerlisting.adapter.out.persistence.repository.SpringDataListingApplicationRepository;
import com.fintrade.core.issuerlisting.adapter.out.persistence.specification.ListingApplicationSpecifications;
import com.fintrade.core.issuerlisting.application.port.in.SearchListingApplicationsUseCase.ListingApplicationFilter;
import com.fintrade.core.issuerlisting.application.port.out.ListingApplicationRepository;
import com.fintrade.core.issuerlisting.domain.model.ListingApplication;
import com.fintrade.core.shared.domain.DomainException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaListingApplicationRepositoryAdapter implements ListingApplicationRepository {

    private final SpringDataListingApplicationRepository repository;
    private final SpringDataIssuerCompanyRepository issuerCompanyRepository;

    public JpaListingApplicationRepositoryAdapter(
            SpringDataListingApplicationRepository repository,
            SpringDataIssuerCompanyRepository issuerCompanyRepository
    ) {
        this.repository = repository;
        this.issuerCompanyRepository = issuerCompanyRepository;
    }

    @Override
    public ListingApplication save(ListingApplication listingApplication) {
        var entity = repository.findById(listingApplication.id()).orElseGet(ListingApplicationEntity::new);
        entity.setId(listingApplication.id());
        entity.setIssuerCompany(issuerCompanyRepository.findById(listingApplication.issuerCompanyId())
                .orElseThrow(() -> new DomainException("Issuer company not found")));
        entity.setTicker(listingApplication.ticker());
        entity.setDisplayName(listingApplication.displayName());
        entity.setParValue(listingApplication.parValue());
        entity.setLotSize(listingApplication.lotSize());
        entity.setCurrency(listingApplication.currency());
        entity.setMarket(listingApplication.market());
        entity.setCriteriaSatisfied(listingApplication.criteriaSatisfied());
        entity.setCriteriaSummary(listingApplication.criteriaSummary());
        entity.setStatus(listingApplication.status());
        entity.setSubmittedBy(listingApplication.submittedBy());
        entity.setReviewedBy(listingApplication.reviewedBy());
        entity.setRejectionReason(listingApplication.rejectionReason());
        entity.setSubmittedAt(listingApplication.submittedAt());
        entity.setReviewedAt(listingApplication.reviewedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<ListingApplication> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ListingApplication> search(ListingApplicationFilter filter) {
        return repository.findAll(ListingApplicationSpecifications.withFilter(filter)).stream()
                .map(this::toDomain)
                .toList();
    }

    private ListingApplication toDomain(ListingApplicationEntity entity) {
        return new ListingApplication(
                entity.getId(),
                entity.getIssuerCompany().getId(),
                entity.getTicker(),
                entity.getDisplayName(),
                entity.getParValue(),
                entity.getLotSize(),
                entity.getCurrency(),
                entity.getMarket(),
                entity.isCriteriaSatisfied(),
                entity.getCriteriaSummary(),
                entity.getStatus(),
                entity.getSubmittedBy(),
                entity.getReviewedBy(),
                entity.getRejectionReason(),
                entity.getSubmittedAt(),
                entity.getReviewedAt()
        );
    }
}
