package com.fintrade.core.issuerlisting.application.port.in;

import java.util.UUID;

public interface ApproveListingApplicationUseCase {

    UUID handle(ApproveListingApplicationCommand command);

    record ApproveListingApplicationCommand(UUID listingApplicationId, String reviewer) {
    }
}
