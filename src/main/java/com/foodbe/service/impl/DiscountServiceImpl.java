package com.foodbe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodbe.DTO.*;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.entity.*;
import com.foodbe.repository.*;
import com.foodbe.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepo;

    @Autowired
    private DiscountTargetRepository targetRepo;

    @Autowired
    private DiscountUsageRepository usageRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ApiResponse<List<DiscountDTO>> getAllDiscounts() {
        List<DiscountDTO> list = discountRepo.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ApiResponse.buildSuccessResponse(list);
    }

    @Transactional
    @Override
    public ApiResponse<DiscountDTO> createDiscount(DiscountDTO dto) {
        DiscountEntity e = fromDTO(dto);
        discountRepo.save(e);
        return ApiResponse.buildSuccessResponse(toDTO(e));
    }

    @Transactional
    @Override
    public ApiResponse<DiscountDTO> updateDiscount(Long id, DiscountDTO dto) {
        DiscountEntity e = discountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi id " + id));
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setDiscountValue(dto.getDiscountValue());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setConditions(toJson(dto.getConditions()));
        discountRepo.save(e);
        return ApiResponse.buildSuccessResponse(toDTO(e));
    }

    @Transactional
    @Override
    public ApiResponse<String> deleteDiscount(Long id) {
        if (!discountRepo.existsById(id)) {
            return ApiResponse.buildErrorResponse(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi id " + id);
        }
        discountRepo.deleteById(id);
        return ApiResponse.buildSuccessResponse("Xóa khuyến mãi thành công");
    }

    @Override
    public ApiResponse<?> applyCoupon(String code, Long userId, BigDecimal orderTotal) {
        DiscountEntity coupon = discountRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));

        LocalDateTime now = LocalDateTime.now();
        if (!Boolean.TRUE.equals(coupon.getActive())
                || (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate()))
                || (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate()))) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết hạn hoặc chưa đến hạn");
        }

        if (coupon.getMinOrderValue() != null && orderTotal.compareTo(coupon.getMinOrderValue()) < 0) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Đơn hàng chưa đạt giá trị tối thiểu");
        }

        long usedCount = usageRepo.countByDiscountIdAndUserId(coupon.getId(), userId);
        if (coupon.getUsageLimit() != null && usedCount >= coupon.getUsageLimit()) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Bạn đã dùng mã này tối đa số lần cho phép");
        }

        BigDecimal discountAmount = calculateDiscount(orderTotal, coupon);
        BigDecimal finalTotal = orderTotal.subtract(discountAmount).max(BigDecimal.ZERO);

        DiscountUsageEntity usage = new DiscountUsageEntity();
        usage.setDiscount(coupon);
        usage.setUserId(userId);
        usageRepo.save(usage);

        Map<String, Object> data = new HashMap<>();
        data.put("discountAmount", discountAmount);
        data.put("finalTotal", finalTotal);
        data.put("code", code);

        return ApiResponse.buildSuccessResponse(data);
    }

    private BigDecimal calculateDiscount(BigDecimal total, DiscountEntity d) {
        BigDecimal discount = BigDecimal.ZERO;
        if ("PERCENT".equalsIgnoreCase(d.getDiscountType())) {
            discount = total.multiply(d.getDiscountValue()).divide(BigDecimal.valueOf(100));
            if (d.getMaxDiscount() != null && discount.compareTo(d.getMaxDiscount()) > 0)
                discount = d.getMaxDiscount();
        } else {
            discount = d.getDiscountValue();
        }
        return discount;
    }

    private DiscountDTO toDTO(DiscountEntity e) {
        DiscountDTO dto = new DiscountDTO();
        dto.setId(e.getId());
        dto.setCode(e.getCode());
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setScope(e.getScope());
        dto.setDiscountType(e.getDiscountType());
        dto.setDiscountValue(e.getDiscountValue());
        dto.setActive(e.getActive());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        dto.setConditions(fromJson(e.getConditions()));
        return dto;
    }

    private DiscountEntity fromDTO(DiscountDTO dto) {
        DiscountEntity e = new DiscountEntity();
        e.setCode(dto.getCode());
        e.setName(dto.getName());
        e.setScope(dto.getScope());
        e.setDiscountType(dto.getDiscountType());
        e.setDiscountValue(dto.getDiscountValue());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setConditions(toJson(dto.getConditions()));
        return e;
    }

    private String toJson(Object obj) {
        try {
            return obj == null ? null : mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private DiscountConditionDTO fromJson(String json) {
        try {
            return json == null ? null : mapper.readValue(json, DiscountConditionDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
