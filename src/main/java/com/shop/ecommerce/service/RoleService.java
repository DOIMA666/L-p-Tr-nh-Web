package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface RoleService {
    Optional<RoleEntity> findById(Long id);
}

