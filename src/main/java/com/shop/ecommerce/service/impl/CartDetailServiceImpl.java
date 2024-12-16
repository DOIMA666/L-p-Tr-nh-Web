package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.CartDetailEntity;
import com.shop.ecommerce.payload.wrapper.CartDetailWrapper;
import com.shop.ecommerce.repository.CartDetailRepository;
import com.shop.ecommerce.repository.CartRepository;
import com.shop.ecommerce.repository.CustomerRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.service.CartDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CartDetailServiceImpl implements CartDetailService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CartDetailServiceImpl(CartRepository cartRepository, CartDetailRepository cartDetailRepository, ProductRepository productRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {

        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void updateDetail(Long id, Integer quantity) {
        Optional<CartDetailEntity> optionalCartDetailEntity = cartDetailRepository.findById(id);
        List<CartDetailEntity> updatedItems = new ArrayList<>();

        if (optionalCartDetailEntity.isPresent()) {
            CartDetailEntity cartDetailEntity = optionalCartDetailEntity.get();
            cartDetailEntity.setQuantity(quantity);
            cartDetailRepository.save(cartDetailEntity);
            updatedItems.add(cartDetailEntity);
        } else {
            throw new EntityNotFoundException("Not found ID: " + id);
        }

        System.out.println(updatedItems);
    }

    @Override
    public void deleteById(Long id) {
        cartDetailRepository.deleteById(id);
    }

    @Override
    public CartDetailWrapper findAllItemsById(List<String> listIdString) {
        List<Long> listIdLong = new ArrayList<>();
        for (String str : listIdString) {
            try {
                Long value = Long.parseLong(str);
                listIdLong.add(value);
            } catch (NumberFormatException e) {
                e.fillInStackTrace();
            }
        }
        CartDetailWrapper cartDetailWrapper = new CartDetailWrapper();
        cartDetailWrapper.setCartDetailEntities(cartDetailRepository.findAllById(listIdLong));
        return cartDetailWrapper;
    }

    @Override
    public void deleteInCart(CartDetailEntity cartDetailEntity) {
        cartDetailRepository.delete(cartDetailEntity);
    }
}
