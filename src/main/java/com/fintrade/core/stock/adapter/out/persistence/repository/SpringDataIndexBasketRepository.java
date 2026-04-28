package com.fintrade.core.stock.adapter.out.persistence.repository;

import com.fintrade.core.stock.adapter.out.persistence.entity.IndexBasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataIndexBasketRepository extends JpaRepository<IndexBasketEntity, UUID> {
}
