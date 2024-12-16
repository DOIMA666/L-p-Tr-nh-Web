package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.CustomerEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SpringBootApplication

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query("select t from CustomerEntity t where t.status = 1")
    List<CustomerEntity> getAllIfActive();

    @Query("select t from CustomerEntity t")
    List<CustomerEntity> getAll();

    Long countByStatus(Integer status);

    CustomerEntity findByUserEntity_Email(String email);
}
