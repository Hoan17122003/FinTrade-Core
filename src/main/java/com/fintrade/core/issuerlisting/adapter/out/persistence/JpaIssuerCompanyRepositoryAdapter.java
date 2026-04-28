package com.fintrade.core.issuerlisting.adapter.out.persistence;

import com.fintrade.core.issuerlisting.adapter.out.persistence.entity.IssuerCompanyEntity;
import com.fintrade.core.issuerlisting.adapter.out.persistence.repository.SpringDataIssuerCompanyRepository;
import com.fintrade.core.issuerlisting.application.port.out.IssuerCompanyRepository;
import com.fintrade.core.issuerlisting.domain.model.IssuerCompany;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaIssuerCompanyRepositoryAdapter implements IssuerCompanyRepository {

    private final SpringDataIssuerCompanyRepository repository;

    public JpaIssuerCompanyRepositoryAdapter(SpringDataIssuerCompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public IssuerCompany save(IssuerCompany issuerCompany) {
        var entity = repository.findById(issuerCompany.id()).orElseGet(IssuerCompanyEntity::new);
        entity.setId(issuerCompany.id());
        entity.setLegalName(issuerCompany.legalName());
        entity.setBusinessRegistrationNumber(issuerCompany.businessRegistrationNumber());
        entity.setPrimarySector(issuerCompany.primarySector());
        entity.setStatus(issuerCompany.status());
        entity.setCreatedAt(issuerCompany.createdAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<IssuerCompany> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<IssuerCompany> findByBusinessRegistrationNumber(String businessRegistrationNumber) {
        return repository.findByBusinessRegistrationNumber(businessRegistrationNumber).map(this::toDomain);
    }

    private IssuerCompany toDomain(IssuerCompanyEntity entity) {
        return new IssuerCompany(
                entity.getId(),
                entity.getLegalName(),
                entity.getBusinessRegistrationNumber(),
                entity.getPrimarySector(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
