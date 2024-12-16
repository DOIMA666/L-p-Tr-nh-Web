package com.shop.ecommerce.service.impl;

import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.repository.ProductImageRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.service.ProductImageService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;

    public ProductImageServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<ProductImageEntity> findByProductId(Long id) {
        return productImageRepository.findAllByProductEntity_Id(id);
    }
}
