package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.AddressEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
