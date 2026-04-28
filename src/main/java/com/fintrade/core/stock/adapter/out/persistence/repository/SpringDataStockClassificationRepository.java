package com.fintrade.core.stock.adapter.out.persistence.repository;

import com.fintrade.core.stock.adapter.out.persistence.entity.StockClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataStockClassificationRepository extends JpaRepository<StockClassificationEntity, UUID> {
}
