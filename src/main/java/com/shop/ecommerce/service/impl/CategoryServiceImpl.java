package com.shop.ecommerce.service.impl;

import com.shop.ecommerce.entity.CategoryEntity;
import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.payload.dto.CategoryDto;
import com.shop.ecommerce.payload.request.CategoryFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.repository.CategoryRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<CategoryDto> getAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.getAll();
        return categoryEntities.stream().map(categoryEntity -> {
            return modelMapper.map(categoryEntity, CategoryDto.class);
        }).collect(Collectors.toList());
    }

    @Override
    public CategoryEntity findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public BaseResponse<Page<CategoryDto>> getAllByFilter(CategoryFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryEntity> categoryEntities = categoryRepository.findAllByFilter(filterRequest, pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<CategoryDto> categoryDtos = categoryEntities.getContent().stream().map(categoryEntity -> {
            CategoryDto categoryDto = modelMapper.map(categoryEntity, CategoryDto.class);
            categoryDto.setCountProducts(categoryEntity.getProductEntities().size());
            String formattedDate = categoryEntity.getCreatedAt().format(formatter);
            categoryDto.setCreatedDate(formattedDate);
            return categoryDto;
        }).toList();

        Page<CategoryDto> pageData = new PageImpl(categoryDtos, pageable, categoryEntities.getTotalElements());
        BaseResponse<Page<CategoryDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }

    @Override
    public void save(CategoryDto categoryDto, String username) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCreatedAt(LocalDateTime.now());
        categoryEntity.setUpdatedAt(LocalDateTime.now());
        categoryEntity.setCreatedBy(username);
        categoryEntity.setUpdatedBy(username);
        categoryEntity.setCategoryName(categoryDto.getCategoryName());
        categoryEntity.setStatus(categoryDto.getStatus());
        categoryEntity.setProductEntities(new HashSet<>());
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void update(CategoryDto categoryDto, String username) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryDto.getId()).get();
        categoryEntity.setCategoryName(categoryDto.getCategoryName());

        categoryEntity.setStatus(categoryDto.getStatus());
        Set<ProductEntity> productEntities = categoryEntity.getProductEntities();
        if (categoryDto.getStatus() == 1) {
            for (ProductEntity p : productEntities) {
                p.setStatus(1);
            }
        } else {
            for (ProductEntity p : productEntities) {
                p.setStatus(0);
            }
        }
        productEntities = new HashSet<>(productRepository.saveAll(productEntities));
        categoryEntity.setProductEntities(productEntities);

        categoryEntity.setUpdatedAt(LocalDateTime.now());
        categoryEntity.setUpdatedBy(username);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public CategoryDto findDtoById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).get();
        CategoryDto categoryDto = modelMapper.map(categoryEntity, CategoryDto.class);
        categoryDto.setCountProducts(categoryEntity.getProductEntities().size());
        return categoryDto;
    }

    @Override
    public void deleteById(Long id, String username) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).get();
        categoryEntity.setStatus(0);
        categoryEntity.setUpdatedAt(LocalDateTime.now());
        categoryEntity.setUpdatedBy(username);

        Set<ProductEntity> productEntities = categoryEntity.getProductEntities();
        for (ProductEntity p : productEntities) {
            p.setStatus(0);
        }
        productEntities = new HashSet<>(productRepository.saveAll(productEntities));
        categoryEntity.setProductEntities(productEntities);
        categoryRepository.save(categoryEntity);
    }
}
