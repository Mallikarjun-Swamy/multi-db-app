package com.ms.service;

import com.ms.dto.request.ProductRequestDto;
import com.ms.dto.response.ProductResponseDto;
import com.ms.mapper.ProductMapper;
import com.ms.model.oracle.Product;
import com.ms.repository.oracle.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = mapper.toEntity(dto);
        System.out.println("product saved to oracle");
        return mapper.toDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAllProducts() {
        return mapper.toDtoList(productRepository.findAll());
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not Found with id " + id));
        return mapper.toDto(product);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not Found with id " + id));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return mapper.toDto(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
