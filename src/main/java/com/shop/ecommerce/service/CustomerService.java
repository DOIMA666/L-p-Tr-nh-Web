package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.CustomerEntity;
import com.shop.ecommerce.payload.dto.CustomerDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public interface CustomerService {
    CustomerEntity saveEntity(CustomerEntity customerEntity);
    Long countCustomers();

    List<CustomerDto> getAll();
    CustomerDto getProfile(String email);
    void updateProfile(CustomerDto customerDto, MultipartFile file, String email);
    Long getIdByEmail(String email);
    CustomerEntity findByEmail(String email);
    Boolean checkExistOrder(Long userId, Long productId);
    CustomerDto findById(Long customerId);
}

