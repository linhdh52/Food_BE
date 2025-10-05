package com.foodbe.service;

import com.foodbe.DTO.ProductDTO;
import com.foodbe.DTO.response.ApiResponse;

import java.util.List;

public interface ProductService {
    ApiResponse<List<ProductDTO>> getAllProducts();
    ApiResponse<ProductDTO> createProduct(ProductDTO productDTO);
    ApiResponse<ProductDTO> updateProduct(ProductDTO productDTO);
    ApiResponse<String> deleteProduct(Long id);
}
