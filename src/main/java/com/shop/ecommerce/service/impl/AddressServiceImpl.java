package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.AddressEntity;
import com.shop.ecommerce.repository.AddressRepository;
import com.shop.ecommerce.service.AddressService;
import org.springframework.stereotype.Service;


@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressEntity saveAddress(AddressEntity addressEntity) {
        return addressRepository.save(addressEntity);
    }
}
