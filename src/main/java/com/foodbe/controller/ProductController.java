package com.foodbe.controller;

import com.foodbe.DTO.ProductDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products API", description = "Quản lý sản phẩm")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    @Operation(summary = "Lấy toàn bộ danh sách sản phẩm")
    public ApiResponse<List<ProductDTO>> getAll() {
        return productService.getAllProducts();
    }

    @PostMapping("/create")
    @Operation(summary = "Tạo sản phẩm")
    public ApiResponse<ProductDTO> create(@RequestBody ProductDTO dto) {
        dto.setId(null);
        return productService.createProduct(dto);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Chỉnh sửa sản phẩm")
    public ApiResponse<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        dto.setId(id);
        return productService.updateProduct(dto);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xóa sản phẩm theo ID")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
