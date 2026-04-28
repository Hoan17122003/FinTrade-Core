package com.fintrade.core.issuerlisting.application.service;

import com.fintrade.core.issuerlisting.application.port.in.ApproveListingApplicationUseCase;
import com.fintrade.core.issuerlisting.application.port.in.CreateIssuerCompanyUseCase;
import com.fintrade.core.issuerlisting.application.port.in.SearchListingApplicationsUseCase;
import com.fintrade.core.issuerlisting.application.port.in.SubmitListingApplicationUseCase;
import com.fintrade.core.issuerlisting.application.port.out.IssuerCompanyRepository;
import com.fintrade.core.issuerlisting.application.port.out.ListingApplicationRepository;
import com.fintrade.core.issuerlisting.application.query.ListingApplicationView;
import com.fintrade.core.issuerlisting.domain.model.IssuerCompany;
import com.fintrade.core.shared.domain.DomainException;
import com.fintrade.core.stock.application.port.out.StockRepository;
import com.fintrade.core.stock.domain.model.Stock;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IssuerListingApplicationService implements
        CreateIssuerCompanyUseCase,
        SubmitListingApplicationUseCase,
        ApproveListingApplicationUseCase,
        SearchListingApplicationsUseCase {

    private final IssuerCompanyRepository issuerCompanyRepository;
    private final ListingApplicationRepository listingApplicationRepository;
    private final StockRepository stockRepository;

    public IssuerListingApplicationService(
            IssuerCompanyRepository issuerCompanyRepository,
            ListingApplicationRepository listingApplicationRepository,
            StockRepository stockRepository
    ) {
        this.issuerCompanyRepository = issuerCompanyRepository;
        this.listingApplicationRepository = listingApplicationRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public UUID handle(CreateIssuerCompanyCommand command) {
        issuerCompanyRepository.findByBusinessRegistrationNumber(command.businessRegistrationNumber())
                .ifPresent(existing -> {
                    throw new DomainException("Issuer company already exists");
                });

        var issuerCompany = IssuerCompany.register(
                command.legalName(),
                command.businessRegistrationNumber(),
                command.primarySector()
        );
        return issuerCompanyRepository.save(issuerCompany).id();
    }

    @Override
    public UUID handle(SubmitListingApplicationCommand command) {
        issuerCompanyRepository.findById(command.issuerCompanyId())
                .orElseThrow(() -> new DomainException("Issuer company not found"));

        var listingApplication = com.fintrade.core.issuerlisting.domain.model.ListingApplication.submit(
                command.issuerCompanyId(),
                command.ticker(),
                command.displayName(),
                command.parValue(),
                command.lotSize(),
                command.currency(),
                command.market(),
                command.criteriaSatisfied(),
                command.criteriaSummary(),
                command.submittedBy()
        );
        return listingApplicationRepository.save(listingApplication).id();
    }

    @Override
    public UUID handle(ApproveListingApplicationCommand command) {
        var application = listingApplicationRepository.findById(command.listingApplicationId())
                .orElseThrow(() -> new DomainException("Listing application not found"));
        var issuerCompany = issuerCompanyRepository.findById(application.issuerCompanyId())
                .orElseThrow(() -> new DomainException("Issuer company not found"));

        var approvedApplication = application.approve(command.reviewer());
        var listedCompany = issuerCompany.markEligible().markListed();
        var stock = Stock.createApproved(
                approvedApplication.issuerCompanyId(),
                approvedApplication.ticker(),
                approvedApplication.displayName(),
                approvedApplication.parValue(),
                approvedApplication.lotSize(),
                approvedApplication.currency(),
                approvedApplication.market()
        ).activate();

        issuerCompanyRepository.save(listedCompany);
        stockRepository.save(stock);
        listingApplicationRepository.save(approvedApplication.activate());

        return stock.id();
    }

    @Override
    public List<ListingApplicationView> handle(ListingApplicationFilter filter) {
        return listingApplicationRepository.search(filter).stream()
                .map(application -> {
                    var issuerName = issuerCompanyRepository.findById(application.issuerCompanyId())
                            .map(IssuerCompany::legalName)
                            .orElse("UNKNOWN");
                    return new ListingApplicationView(
                            application.id(),
                            application.issuerCompanyId(),
                            issuerName,
                            application.ticker(),
                            application.displayName(),
                            application.status(),
                            application.criteriaSatisfied(),
                            application.criteriaSummary(),
                            application.submittedAt(),
                            application.reviewedAt()
                    );
                })
                .toList();
    }
}
