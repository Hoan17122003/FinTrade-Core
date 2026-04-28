package com.fintrade.core.portfolio.adapter.out.persistence.repository;

import com.fintrade.core.portfolio.adapter.out.persistence.entity.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPortfolioRepository extends JpaRepository<PortfolioEntity, UUID> {
}
