package com.ms.controller;

import com.ms.dto.request.ProductRequestDto;
import com.ms.dto.response.ApiResponse;
import com.ms.dto.response.ProductResponseDto;
import com.ms.dto.response.ResponseUtil;
import com.ms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // CREATE PRODUCT
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @RequestBody ProductRequestDto request) {

        ProductResponseDto product = productService.createProduct(request);

        return ResponseUtil.success(
                HttpStatus.CREATED,
                "Product created successfully",
                product
        );
    }

    // GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {

        List<ProductResponseDto> products = productService.getAllProducts();

        return ResponseUtil.success(
                HttpStatus.OK,
                "Products fetched successfully",
                products
        );
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(
            @PathVariable Long id) {

        ProductResponseDto product = productService.getProductById(id);

        return ResponseUtil.success(
                HttpStatus.OK,
                "Product fetched successfully",
                product
        );
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDto request) {

        ProductResponseDto updatedProduct =
                productService.updateProduct(id, request);

        return ResponseUtil.success(
                HttpStatus.OK,
                "Product updated successfully",
                updatedProduct
        );
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseUtil.success(
                HttpStatus.OK,
                "Product deleted successfully",
                null
        );
    }

}