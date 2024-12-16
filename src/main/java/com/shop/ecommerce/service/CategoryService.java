package com.shop.ecommerce.service;


import com.shop.ecommerce.entity.CategoryEntity;
import com.shop.ecommerce.payload.dto.CategoryDto;
import com.shop.ecommerce.payload.request.CategoryFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll();
    CategoryEntity findById(Long id);
    BaseResponse<Page<CategoryDto>> getAllByFilter(CategoryFilterRequest filterRequest, int page, int size);

    void save(CategoryDto categoryDto, String username);
    void update(CategoryDto categoryDto, String username);
    CategoryDto findDtoById(Long id);
    void deleteById(Long id, String username);
}
