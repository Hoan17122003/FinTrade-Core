package com.fintrade.core.issuerlisting.application.port.out;

import com.fintrade.core.issuerlisting.application.port.in.SearchListingApplicationsUseCase.ListingApplicationFilter;
import com.fintrade.core.issuerlisting.domain.model.ListingApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingApplicationRepository {

    ListingApplication save(ListingApplication listingApplication);

    Optional<ListingApplication> findById(UUID id);

    List<ListingApplication> search(ListingApplicationFilter filter);
}
