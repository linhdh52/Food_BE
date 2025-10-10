package com.foodbe.service;

import com.foodbe.DTO.DiscountDTO;
import com.foodbe.DTO.response.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {
    ApiResponse<List<DiscountDTO>> getAllDiscounts();
    ApiResponse<DiscountDTO> createDiscount(DiscountDTO dto);
    ApiResponse<DiscountDTO> updateDiscount(DiscountDTO dto);
    ApiResponse<String> deleteDiscount(Long id);
    ApiResponse<?> applyCoupon(String code, Long userId, BigDecimal orderTotal, String paymentMethod, String regionCode, String userSegment);
}
