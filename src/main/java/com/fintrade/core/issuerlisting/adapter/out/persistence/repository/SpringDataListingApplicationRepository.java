package com.fintrade.core.issuerlisting.adapter.out.persistence.repository;

import com.fintrade.core.issuerlisting.adapter.out.persistence.entity.ListingApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataListingApplicationRepository extends JpaRepository<ListingApplicationEntity, UUID>,
        JpaSpecificationExecutor<ListingApplicationEntity> {
}
