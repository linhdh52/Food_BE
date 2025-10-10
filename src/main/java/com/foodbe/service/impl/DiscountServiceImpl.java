package com.foodbe.service.impl;

import com.foodbe.DTO.DiscountDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.entity.DiscountEntity;
import com.foodbe.entity.DiscountUsageEntity;
import com.foodbe.repository.DiscountRepository;
import com.foodbe.repository.DiscountUsageRepository;
import com.foodbe.service.DiscountService;
import com.foodbe.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepo;

    @Autowired
    private DiscountUsageRepository usageRepo;

    @Override
    public ApiResponse<List<DiscountDTO>> getAllDiscounts() {
        List<DiscountDTO> list = discountRepo.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ApiResponse.buildSuccessResponse(list);
    }

    @Transactional
    @Override
    public ApiResponse<DiscountDTO> createDiscount(DiscountDTO dto) {
        DiscountEntity e = fromDTO(dto, new DiscountEntity());
        discountRepo.save(e);
        return ApiResponse.buildSuccessResponse(toDTO(e));
    }

    @Transactional
    @Override
    public ApiResponse<DiscountDTO> updateDiscount(DiscountDTO dto) {
        DiscountEntity e = discountRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi id " + dto.getId()));
        e = fromDTO(dto, e);
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

    // ===== Áp mã giảm giá cho tổng đơn =====
    @Override
    public ApiResponse<?> applyCoupon(String code, Long userId, BigDecimal orderTotal,
                                      String paymentMethod, String regionCode, String userSegment) {
        DiscountEntity d = discountRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));

        // Hiệu lực cơ bản
        LocalDateTime now = LocalDateTime.now();
        if (!Boolean.TRUE.equals(d.getActive())
                || (d.getStartDate() != null && now.isBefore(d.getStartDate()))
                || (d.getEndDate() != null && now.isAfter(d.getEndDate()))) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết hạn hoặc chưa đến hạn");
        }

        if (!"ORDER".equalsIgnoreCase(d.getScope())) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Mã này không áp dụng ở cấp đơn hàng");
        }

        if (d.getMinOrderValue() != null && orderTotal.compareTo(d.getMinOrderValue()) < 0) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Đơn hàng chưa đạt giá trị tối thiểu");
        }

        // BỎ kiểm tra dayOfWeek theo yêu cầu — FE đẩy gì BE lưu vậy, không check
        // Nếu muốn bỏ luôn khung giờ, xoá block dưới
        if (!matchTime(d, LocalTime.now())) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Mã chỉ áp dụng trong khung giờ quy định");
        }

        // Check user theo CSV (nếu có cấu hình)
        if (!matchUserCsv(d.getUserId(), userId)) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Tài khoản không nằm trong danh sách áp dụng");
        }

        // Giới hạn số lần dùng theo user
        long usedByUser = usageRepo.countByDiscountIdAndUserId(d.getId(), userId);
        if (d.getMaxUsagePerUser() != null && usedByUser >= d.getMaxUsagePerUser()) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Bạn đã dùng mã này tối đa số lần cho phép");
        }

        // Segment/Payment/Region (nếu có)
        if (d.getSegmentCode() != null && (userSegment == null || !d.getSegmentCode().equalsIgnoreCase(userSegment))) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Mã không áp dụng cho phân khúc hiện tại");
        }
        if (d.getPaymentMethod() != null && (paymentMethod == null || !d.getPaymentMethod().equalsIgnoreCase(paymentMethod))) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Phương thức thanh toán không hợp lệ cho mã này");
        }
        if (d.getRegionCode() != null && (regionCode == null || !d.getRegionCode().equalsIgnoreCase(regionCode))) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Khu vực không hợp lệ cho mã này");
        }

        // Tính mức giảm
        BigDecimal discountAmount = calculateDiscount(orderTotal, d);
        BigDecimal finalTotal = orderTotal.subtract(discountAmount);
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) finalTotal = BigDecimal.ZERO;

        DiscountUsageEntity usage = new DiscountUsageEntity();
        usage.setDiscount(d);
        usage.setUserId(userId);
        usageRepo.save(usage);

        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("discountAmount", discountAmount);
        data.put("finalTotal", finalTotal);

        return ApiResponse.buildSuccessResponse(data);
    }

    private boolean matchTime(DiscountEntity d, LocalTime now) {
        if (d.getStartTime() == null || d.getEndTime() == null) return true;
        return !now.isBefore(d.getStartTime()) && !now.isAfter(d.getEndTime());
    }

    private boolean matchUserCsv(String csvUserIds, Long userId) {
        if (csvUserIds == null || csvUserIds.trim().isEmpty()) return true;
        if (userId == null) return false;
        String needle = String.valueOf(userId).trim();
        if (needle.isEmpty()) return false;
        Set<String> allowed = new HashSet<>(CsvUtil.split(csvUserIds));
        return allowed.contains(needle);
    }

    private BigDecimal calculateDiscount(BigDecimal total, DiscountEntity d) {
        BigDecimal discount = BigDecimal.ZERO;
        if ("PERCENT".equalsIgnoreCase(d.getDiscountType())) {
            if (d.getDiscountValue() != null) {
                discount = total.multiply(d.getDiscountValue()).divide(BigDecimal.valueOf(100));
                if (d.getMaxDiscount() != null && discount.compareTo(d.getMaxDiscount()) > 0) {
                    discount = d.getMaxDiscount();
                }
            }
        } else {
            discount = d.getDiscountValue() != null ? d.getDiscountValue() : BigDecimal.ZERO;
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
        dto.setMinOrderValue(e.getMinOrderValue());
        dto.setMaxDiscount(e.getMaxDiscount());
        dto.setActive(e.getActive());
        dto.setStackable(e.getStackable());
        dto.setPriority(e.getPriority());
        dto.setUsageLimit(e.getUsageLimit());
        dto.setUsedCount(e.getUsedCount());
        dto.setMaxUsagePerUser(e.getMaxUsagePerUser());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        dto.setDayOfWeek(CsvUtil.split(e.getDayOfWeek()));
        dto.setUserId(CsvUtil.split(e.getUserId()));
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        dto.setSegmentCode(e.getSegmentCode());
        dto.setPaymentMethod(e.getPaymentMethod());
        dto.setRegionCode(e.getRegionCode());
        return dto;
    }

    private DiscountEntity fromDTO(DiscountDTO dto, DiscountEntity e) {
        e.setCode(dto.getCode());
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setScope(dto.getScope());
        e.setDiscountType(dto.getDiscountType());
        e.setDiscountValue(dto.getDiscountValue());
        e.setMinOrderValue(dto.getMinOrderValue());
        e.setMaxDiscount(dto.getMaxDiscount());
        e.setActive(dto.getActive() != null ? dto.getActive() : Boolean.TRUE);
        e.setStackable(dto.getStackable() != null ? dto.getStackable() : Boolean.FALSE);
        e.setPriority(dto.getPriority() != null ? dto.getPriority() : 100);
        e.setUsageLimit(dto.getUsageLimit());
        e.setMaxUsagePerUser(dto.getMaxUsagePerUser());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setDayOfWeek(CsvUtil.join(dto.getDayOfWeek()));
        e.setUserId(CsvUtil.join(dto.getUserId()));
        e.setStartTime(dto.getStartTime());
        e.setEndTime(dto.getEndTime());
        e.setSegmentCode(dto.getSegmentCode());
        e.setPaymentMethod(dto.getPaymentMethod());
        e.setRegionCode(dto.getRegionCode());
        return e;
    }
}
