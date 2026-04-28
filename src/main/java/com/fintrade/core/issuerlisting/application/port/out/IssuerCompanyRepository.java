package com.fintrade.core.issuerlisting.application.port.out;

import com.fintrade.core.issuerlisting.domain.model.IssuerCompany;

import java.util.Optional;
import java.util.UUID;

public interface IssuerCompanyRepository {

    IssuerCompany save(IssuerCompany issuerCompany);

    Optional<IssuerCompany> findById(UUID id);

    Optional<IssuerCompany> findByBusinessRegistrationNumber(String businessRegistrationNumber);
}
