package com.foodbe.repository;

import com.foodbe.entity.DiscountUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountUsageRepository extends JpaRepository<DiscountUsageEntity, Long> {
    Long countByDiscountIdAndUserId(Long discountId, Long userId);
}
