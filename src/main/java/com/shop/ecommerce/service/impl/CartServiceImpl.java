package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.CartDetailEntity;
import com.shop.ecommerce.entity.CartEntity;
import com.shop.ecommerce.entity.CustomerEntity;
import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.payload.dto.CartDetailDto;
import com.shop.ecommerce.repository.CartDetailRepository;
import com.shop.ecommerce.repository.CartRepository;
import com.shop.ecommerce.repository.CustomerRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository, CartDetailRepository cartDetailRepository, ProductRepository productRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {

        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CartEntity saveCart(CartEntity cartEntity) {
        return cartRepository.save(cartEntity);
    }

    @Override
    public void addToCart(Long productId, String username) {
        CustomerEntity customerEntity = customerRepository.findByUserEntity_Email(username);
        CartEntity cartEntity = cartRepository.findByEmail(username);

        Set<CartDetailEntity> cartDetailEntities = cartEntity.getCartDetailEntities();

        ProductEntity productEntity = productRepository.findById(productId).get();

        // update detail
        CartDetailEntity testCartDetail = new CartDetailEntity();
        boolean flag = false;
        for (CartDetailEntity c : cartDetailEntities) {
            if(Objects.equals(c.getProductEntity().getId(), productId)) {
                testCartDetail = c;
                flag = true;
                break;
            }
        }

        if(flag) {
            testCartDetail.setQuantity(testCartDetail.getQuantity() + 1);
            testCartDetail.setTotalPrice(productEntity.getPrice().multiply(BigDecimal.valueOf(testCartDetail.getQuantity())));
            testCartDetail.setUpdatedAt(LocalDateTime.now());
            testCartDetail.setUpdatedBy(username);
            testCartDetail = cartDetailRepository.save(testCartDetail);

            for (CartDetailEntity c : cartDetailEntities) {
                if(Objects.equals(c.getProductEntity().getId(), productId)) {
                    cartDetailEntities.remove(c);
                    break;
                }
            }
            boolean check = cartDetailEntities.add(testCartDetail);
            System.out.println(check);
        }
        else {
            CartDetailEntity cartDetailEntity = new CartDetailEntity();
            cartDetailEntity.setCartEntity(cartEntity);
            cartDetailEntity.setProductEntity(productEntity);
            cartDetailEntity.setQuantity(1);
            cartDetailEntity.setPriceOfOne(productEntity.getSalePrice());
            cartDetailEntity.setTotalPrice(productEntity.getSalePrice().multiply(BigDecimal.valueOf(1)));
            cartDetailEntity.setStatus(1);
            cartDetailEntity.setCreatedAt(LocalDateTime.now());
            cartDetailEntity.setUpdatedAt(LocalDateTime.now());
            cartDetailEntity.setCreatedBy(username);
            cartDetailEntity.setUpdatedBy(username);
            cartDetailEntity = cartDetailRepository.save(cartDetailEntity);
            boolean check = cartDetailEntities.add(cartDetailEntity);
            System.out.println(check);
        }
        cartEntity.setCartDetailEntities(cartDetailEntities);
        BigDecimal totalCost = BigDecimal.valueOf(0);
        for(CartDetailEntity c : cartDetailEntities) {
            totalCost = totalCost.add(c.getTotalPrice()) ;
        }
        cartEntity.setTotalCost(totalCost);
        cartEntity = cartRepository.save(cartEntity);

        cartEntity.setCustomerEntity(customerEntity);
        cartEntity = cartRepository.save(cartEntity);

        customerEntity.setCartEntity(cartEntity);
//        Update amount
        productEntity.setAmount(productEntity.getAmount() - 1);
        productRepository.save(productEntity);
    }

    @Override
    public List<CartDetailDto> getAllDetails(String username) {
        CartEntity cartEntity = cartRepository.findByEmail(username);
        return cartEntity.getCartDetailEntities().stream().map(cartDetailEntity -> {
            CartDetailDto cartDetailDto = modelMapper.map(cartDetailEntity, CartDetailDto.class);
            cartDetailDto.setCartId(cartDetailEntity.getCartEntity().getId());
            cartDetailDto.setThumbnail(cartDetailEntity.getProductEntity().getThumbnail());
            cartDetailDto.setProductName(cartDetailEntity.getProductEntity().getProductName());
            return cartDetailDto;
        }).collect(Collectors.toList());
    }
}
