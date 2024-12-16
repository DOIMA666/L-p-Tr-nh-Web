package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.OrderDetailEntity;
import com.shop.ecommerce.payload.request.OrderDetailFilterRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    @Query(value = "SELECT od FROM OrderDetailEntity od " +
            "LEFT JOIN od.orderEntity o " +
            "WHERE o.id = :id " +
            "AND (:#{#condition.totalPrice} IS NULL OR od.totalPrice = :#{#condition.totalPrice}) " +
            "AND (:#{#condition.quantity} IS NULL OR od.quantity = :#{#condition.quantity}) " +
            "AND (:#{#condition.priceOfOne} IS NULL OR od.priceOfOne = :#{#condition.priceOfOne}) "
    )
    Page<OrderDetailEntity> findAllByFilter(@Param("condition") OrderDetailFilterRequest filterRequest, @Param("id") Long id, Pageable pageable);


    Boolean existsByOrderEntity_CustomerEntity_UserEntity_IdAndProductEntity_Id(Long customerId, Long productId);
}
