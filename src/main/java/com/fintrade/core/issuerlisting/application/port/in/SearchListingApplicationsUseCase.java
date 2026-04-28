package com.fintrade.core.issuerlisting.application.port.in;

import com.fintrade.core.issuerlisting.application.query.ListingApplicationView;
import com.fintrade.core.issuerlisting.domain.model.ListingApplicationStatus;

import java.util.List;

public interface SearchListingApplicationsUseCase {

    List<ListingApplicationView> handle(ListingApplicationFilter filter);

    record ListingApplicationFilter(ListingApplicationStatus status, String issuerNameKeyword) {
    }
}
