package com.fintrade.core.stock.adapter.out.persistence.specification;

import com.fintrade.core.stock.adapter.out.persistence.entity.StockEntity;
import com.fintrade.core.stock.application.port.in.SearchStocksUseCase.StockFilter;
import org.springframework.data.jpa.domain.Specification;

public final class StockSpecifications {

    private StockSpecifications() {
    }

    public static Specification<StockEntity> withFilter(StockFilter filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();
            if (filter == null) {
                return predicate;
            }

            query.distinct(true);

            if (filter.keyword() != null && !filter.keyword().isBlank()) {
                var keyword = "%" + filter.keyword().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("ticker")), keyword),
                        cb.like(cb.lower(root.get("displayName")), keyword)
                ));
            }
            if (filter.market() != null && !filter.market().isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("market"), filter.market()));
            }
            if (filter.tradingStatus() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("tradingStatus"), filter.tradingStatus()));
            }
            if (filter.issuerCompanyId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("issuerCompany").get("id"), filter.issuerCompanyId()));
            }
            if (filter.classificationCode() != null && !filter.classificationCode().isBlank()) {
                var classificationJoin = root.join("classifications");
                predicate = cb.and(predicate, cb.equal(classificationJoin.get("code"), filter.classificationCode()));
            }
            if (filter.indexCode() != null && !filter.indexCode().isBlank()) {
                var indexJoin = root.join("indexBaskets");
                predicate = cb.and(predicate, cb.equal(indexJoin.get("code"), filter.indexCode()));
            }

            return predicate;
        };
    }
}
