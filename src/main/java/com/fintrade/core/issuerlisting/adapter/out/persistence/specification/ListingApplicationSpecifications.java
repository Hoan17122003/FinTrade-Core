package com.fintrade.core.issuerlisting.adapter.out.persistence.specification;

import com.fintrade.core.issuerlisting.adapter.out.persistence.entity.ListingApplicationEntity;
import com.fintrade.core.issuerlisting.application.port.in.SearchListingApplicationsUseCase.ListingApplicationFilter;
import org.springframework.data.jpa.domain.Specification;

public final class ListingApplicationSpecifications {

    private ListingApplicationSpecifications() {
    }

    public static Specification<ListingApplicationEntity> withFilter(ListingApplicationFilter filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filter == null) {
                return predicate;
            }
            if (filter.status() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), filter.status()));
            }
            if (filter.issuerNameKeyword() != null && !filter.issuerNameKeyword().isBlank()) {
                var issuerJoin = root.join("issuerCompany");
                predicate = cb.and(predicate, cb.like(
                        cb.lower(issuerJoin.get("legalName")),
                        "%" + filter.issuerNameKeyword().toLowerCase() + "%"
                ));
            }
            return predicate;
        };
    }
}
