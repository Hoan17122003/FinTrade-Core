package com.fintrade.core.stock.domain.model;

import com.fintrade.core.shared.domain.DomainException;

import java.util.Objects;
import java.util.UUID;

public record StockClassification(
        UUID id,
        String code,
        String name,
        ClassificationType type,
        String description
) {

    public StockClassification {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(description, "description must not be null");
        if (code.isBlank() || name.isBlank()) {
            throw new DomainException("Classification code and name must not be blank");
        }
    }

    public static StockClassification create(String code, String name, ClassificationType type, String description) {
        return new StockClassification(UUID.randomUUID(), code, name, type, description == null ? "" : description);
    }
}
