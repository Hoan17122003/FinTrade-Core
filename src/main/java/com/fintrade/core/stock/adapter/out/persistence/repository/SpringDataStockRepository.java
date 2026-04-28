package com.fintrade.core.stock.adapter.out.persistence.repository;

import com.fintrade.core.stock.adapter.out.persistence.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataStockRepository extends JpaRepository<StockEntity, UUID>, JpaSpecificationExecutor<StockEntity> {
}
