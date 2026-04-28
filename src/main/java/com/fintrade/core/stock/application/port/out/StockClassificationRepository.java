package com.fintrade.core.stock.application.port.out;

import com.fintrade.core.stock.domain.model.StockClassification;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface StockClassificationRepository {

    StockClassification save(StockClassification stockClassification);

    Optional<StockClassification> findById(UUID id);

    List<StockClassification> findByIds(Set<UUID> ids);
}
