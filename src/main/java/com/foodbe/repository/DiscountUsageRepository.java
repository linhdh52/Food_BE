package com.foodbe.repository;

import com.foodbe.entity.DiscountUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountUsageRepository extends JpaRepository<DiscountUsageEntity, Long> {
    List<DiscountUsageEntity> findByUserId(Long userId);
    Long countByDiscountIdAndUserId(Long discountId, Long userId);
}
