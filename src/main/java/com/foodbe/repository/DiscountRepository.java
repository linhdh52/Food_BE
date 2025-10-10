package com.foodbe.repository;

import com.foodbe.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    Optional<DiscountEntity> findByCode(String code);

    @Query("SELECT d FROM DiscountEntity d WHERE d.active = true AND " +
            "(d.startDate IS NULL OR d.startDate <= :now) AND " +
            "(d.endDate IS NULL OR d.endDate >= :now)")
    List<DiscountEntity> findAllActive(LocalDateTime now);
}
