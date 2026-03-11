package com.ms.mapper;

import com.ms.dto.request.ProductRequestDto;
import com.ms.dto.response.ProductResponseDto;
import com.ms.model.oracle.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDto dto);

    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> list);

}
