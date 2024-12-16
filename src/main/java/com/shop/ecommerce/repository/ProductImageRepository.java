package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.ProductImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SpringBootApplication
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    List<ProductImageEntity> findAllByProductEntity_Id(Long id);

    @Transactional
    void deleteByImageEntity_Id(Long id);

    ProductImageEntity findByImageEntity_Id(Long id);

}
