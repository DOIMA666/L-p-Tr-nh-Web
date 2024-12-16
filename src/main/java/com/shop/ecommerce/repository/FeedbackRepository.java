package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.FeedbackEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SpringBootApplication
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> getAllByProductEntity_IdAndStatus(Long id, Integer status);

    @Query("SELECT f FROM FeedbackEntity f WHERE f.status = 1 ORDER BY f.createdAt DESC")
    List<FeedbackEntity> findTop4Feedback();

    Boolean existsByCustomerEntity_UserEntity_IdAndProductEntity_Id(Long customerId, Long productId);
}
