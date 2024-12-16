package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.CartDetailEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface CartDetailRepository extends JpaRepository<CartDetailEntity, Long> {
    CartDetailEntity findByProductEntity_Id(Long id);
}
