package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.RoleEntity;
import com.shop.ecommerce.repository.RoleRepository;
import com.shop.ecommerce.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Optional<RoleEntity> findById(Long id) {
        return roleRepository.findById(id);
    }
}
