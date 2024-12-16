package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.CartEntity;
import com.shop.ecommerce.payload.dto.CartDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    CartEntity saveCart(CartEntity cartEntity);
    void addToCart(Long productId, String username);
    List<CartDetailDto> getAllDetails(String username);
}

