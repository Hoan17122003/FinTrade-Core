package com.fintrade.core.stock.domain.model;

import com.fintrade.core.shared.domain.DomainException;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record IndexBasket(
        UUID id,
        String code,
        String name,
        String description,
        Set<UUID> stockIds
) {

    public IndexBasket {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(description, "description must not be null");
        stockIds = Set.copyOf(stockIds);
        if (code.isBlank() || name.isBlank()) {
            throw new DomainException("Index basket code and name must not be blank");
        }
    }

    public static IndexBasket create(String code, String name, String description) {
        return new IndexBasket(UUID.randomUUID(), code, name, description == null ? "" : description, Set.of());
    }

    public IndexBasket addStock(UUID stockId) {
        var updated = new LinkedHashSet<>(stockIds);
        updated.add(stockId);
        return new IndexBasket(id, code, name, description, updated);
    }
}
