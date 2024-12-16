package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.ProductImageEntity;

import java.util.List;

public interface ProductImageService {
    List<ProductImageEntity> findByProductId(Long id);
}
