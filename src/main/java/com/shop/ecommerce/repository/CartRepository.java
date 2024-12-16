package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.CartEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("select c " +
            "from CartEntity c " +
            "left join c.cartDetailEntities cd " +
            "left join c.customerEntity cu " +
            "left join cu.userEntity u " +
            "where u.email = :email")
    CartEntity findByEmail(@Param("email") String email);
}
