package com.foodbe.repository;

import com.foodbe.entity.DiscountTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountTargetRepository extends JpaRepository<DiscountTargetEntity, Long> {
    List<DiscountTargetEntity> findByDiscountId(Long discountId);
    List<DiscountTargetEntity> findByTargetTypeAndTargetId(String targetType, Long targetId);
}
