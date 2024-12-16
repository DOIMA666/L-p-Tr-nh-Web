package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.RoleEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@SpringBootApplication
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findById(Long id);
}
