package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.ImageEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
