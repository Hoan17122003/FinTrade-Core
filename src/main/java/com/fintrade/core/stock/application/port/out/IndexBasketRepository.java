package com.fintrade.core.stock.application.port.out;

import com.fintrade.core.stock.domain.model.IndexBasket;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IndexBasketRepository {

    IndexBasket save(IndexBasket indexBasket);

    Optional<IndexBasket> findById(UUID id);

    List<IndexBasket> findByIds(Set<UUID> ids);
}
