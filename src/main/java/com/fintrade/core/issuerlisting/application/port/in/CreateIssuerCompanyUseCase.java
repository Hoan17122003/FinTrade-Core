package com.fintrade.core.issuerlisting.application.port.in;

import java.util.UUID;

public interface CreateIssuerCompanyUseCase {

    UUID handle(CreateIssuerCompanyCommand command);

    record CreateIssuerCompanyCommand(String legalName, String businessRegistrationNumber, String primarySector) {
    }
}
