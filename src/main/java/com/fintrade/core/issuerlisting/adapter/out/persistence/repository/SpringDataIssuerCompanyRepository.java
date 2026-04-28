package com.fintrade.core.issuerlisting.adapter.out.persistence.repository;

import com.fintrade.core.issuerlisting.adapter.out.persistence.entity.IssuerCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataIssuerCompanyRepository extends JpaRepository<IssuerCompanyEntity, UUID> {

    Optional<IssuerCompanyEntity> findByBusinessRegistrationNumber(String businessRegistrationNumber);
}
