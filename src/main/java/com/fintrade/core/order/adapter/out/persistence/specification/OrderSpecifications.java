package com.fintrade.core.order.adapter.out.persistence.specification;

import com.fintrade.core.order.adapter.out.persistence.entity.OrderEntity;
import com.fintrade.core.order.application.port.in.SearchOrdersUseCase.OrderFilter;
import org.springframework.data.jpa.domain.Specification;

public final class OrderSpecifications {

    private OrderSpecifications() {
    }

    public static Specification<OrderEntity> withFilter(OrderFilter filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();
            if (filter == null) {
                return predicate;
            }
            if (filter.userId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("userId"), filter.userId()));
            }
            if (filter.stockId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("stockId"), filter.stockId()));
            }
            if (filter.side() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("side"), filter.side()));
            }
            if (filter.status() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), filter.status()));
            }
            return predicate;
        };
    }
}
