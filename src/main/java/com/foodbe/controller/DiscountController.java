package com.foodbe.controller;

import com.foodbe.DTO.DiscountDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/discounts/")
@Tag(name = "Discount API", description = "Quản lý chương trình khuyến mãi / giảm giá")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("getAll")
    @Operation(summary = "Lấy danh sách tất cả khuyến mãi")
    public ApiResponse<List<DiscountDTO>> getAll() {
        return discountService.getAllDiscounts();
    }

    @PostMapping("create")
    @Operation(summary = "Tạo khuyến mãi mới")
    public ApiResponse<DiscountDTO> create(@RequestBody DiscountDTO dto) {
        return discountService.createDiscount(dto);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Cập nhật khuyến mãi")
    public ApiResponse<DiscountDTO> update(@PathVariable Long id, @RequestBody DiscountDTO dto) {
        return discountService.updateDiscount(id, dto);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Xoá khuyến mãi")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return discountService.deleteDiscount(id);
    }

    @PostMapping("apply-coupon")
    @Operation(summary = "Áp mã giảm giá cho tổng đơn hàng")
    public ApiResponse<?> applyCoupon(@RequestParam String code,
                                      @RequestParam Long userId,
                                      @RequestParam BigDecimal orderTotal) {
        return discountService.applyCoupon(code, userId, orderTotal);
    }
}
