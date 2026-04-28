package com.fintrade.core.issuerlisting.adapter.in.web;

import com.fintrade.core.issuerlisting.application.port.in.ApproveListingApplicationUseCase;
import com.fintrade.core.issuerlisting.application.port.in.CreateIssuerCompanyUseCase;
import com.fintrade.core.issuerlisting.application.port.in.SearchListingApplicationsUseCase;
import com.fintrade.core.issuerlisting.application.port.in.SubmitListingApplicationUseCase;
import com.fintrade.core.issuerlisting.application.query.ListingApplicationView;
import com.fintrade.core.issuerlisting.domain.model.ListingApplicationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminIssuerListingController {

    private final CreateIssuerCompanyUseCase createIssuerCompanyUseCase;
    private final SubmitListingApplicationUseCase submitListingApplicationUseCase;
    private final ApproveListingApplicationUseCase approveListingApplicationUseCase;
    private final SearchListingApplicationsUseCase searchListingApplicationsUseCase;

    public AdminIssuerListingController(
            CreateIssuerCompanyUseCase createIssuerCompanyUseCase,
            SubmitListingApplicationUseCase submitListingApplicationUseCase,
            ApproveListingApplicationUseCase approveListingApplicationUseCase,
            SearchListingApplicationsUseCase searchListingApplicationsUseCase
    ) {
        this.createIssuerCompanyUseCase = createIssuerCompanyUseCase;
        this.submitListingApplicationUseCase = submitListingApplicationUseCase;
        this.approveListingApplicationUseCase = approveListingApplicationUseCase;
        this.searchListingApplicationsUseCase = searchListingApplicationsUseCase;
    }

    @PostMapping("/issuers")
    public IdResponse createIssuer(@Valid @RequestBody CreateIssuerRequest request) {
        var id = createIssuerCompanyUseCase.handle(new CreateIssuerCompanyUseCase.CreateIssuerCompanyCommand(
                request.legalName(),
                request.businessRegistrationNumber(),
                request.primarySector()
        ));
        return new IdResponse(id);
    }

    @PostMapping("/listings")
    public IdResponse submitListing(@Valid @RequestBody SubmitListingRequest request) {
        var id = submitListingApplicationUseCase.handle(new SubmitListingApplicationUseCase.SubmitListingApplicationCommand(
                request.issuerCompanyId(),
                request.ticker(),
                request.displayName(),
                request.parValue(),
                request.lotSize(),
                request.currency(),
                request.market(),
                request.criteriaSatisfied(),
                request.criteriaSummary(),
                request.submittedBy()
        ));
        return new IdResponse(id);
    }

    @PostMapping("/listings/{listingApplicationId}/approve")
    public IdResponse approveListing(@PathVariable UUID listingApplicationId, @Valid @RequestBody ApproveListingRequest request) {
        var stockId = approveListingApplicationUseCase.handle(
                new ApproveListingApplicationUseCase.ApproveListingApplicationCommand(listingApplicationId, request.reviewer())
        );
        return new IdResponse(stockId);
    }

    @GetMapping("/listings")
    public List<ListingApplicationView> searchListings(
            @RequestParam(required = false) ListingApplicationStatus status,
            @RequestParam(required = false) String issuerName
    ) {
        return searchListingApplicationsUseCase.handle(
                new SearchListingApplicationsUseCase.ListingApplicationFilter(status, issuerName)
        );
    }

    public record CreateIssuerRequest(
            @NotBlank String legalName,
            @NotBlank String businessRegistrationNumber,
            @NotBlank String primarySector
    ) {
    }

    public record SubmitListingRequest(
            @NotNull UUID issuerCompanyId,
            @NotBlank String ticker,
            @NotBlank String displayName,
            @NotNull @DecimalMin("0.01") BigDecimal parValue,
            @Min(1) int lotSize,
            @NotBlank String currency,
            @NotBlank String market,
            boolean criteriaSatisfied,
            @NotBlank String criteriaSummary,
            @NotBlank String submittedBy
    ) {
    }

    public record ApproveListingRequest(@NotBlank String reviewer) {
    }

    public record IdResponse(UUID id) {
    }
}
