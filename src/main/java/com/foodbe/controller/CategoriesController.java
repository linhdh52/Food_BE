package com.foodbe.controller;

import com.foodbe.DTO.CategoriesDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories API", description = "Quản lý danh mục sản phẩm")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/getAll")
    @Operation(summary = "Lấy toàn bộ danh sách category")
    public ApiResponse<List<CategoriesDTO>> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    @PostMapping("/create")
    @Operation(summary = "Tạo mới danh mục")
    public ApiResponse<CategoriesDTO> createCategory(@RequestBody CategoriesDTO categoryDTO) {
        return categoriesService.createCategory(categoryDTO);
    }

    @PostMapping("/update")
    @Operation(summary = "Cập nhật category theo id")
    public ApiResponse<CategoriesDTO> updateCategory(@RequestBody CategoriesDTO categoryDTO) {
        return categoriesService.updateCategory(categoryDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xoá category theo id")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        return categoriesService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết category theo id")
    public ApiResponse<CategoriesDTO> getCategoryById(@PathVariable Long id) {
        return categoriesService.getCategoryById(id);
    }
}
