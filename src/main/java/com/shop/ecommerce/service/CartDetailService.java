package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.CartDetailEntity;
import com.shop.ecommerce.payload.wrapper.CartDetailWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartDetailService {
    void updateDetail(Long id, Integer quantity);

    void deleteById(Long id);
    CartDetailWrapper findAllItemsById(List<String> listIdString);
    void deleteInCart(CartDetailEntity cartDetailEntity);
}

