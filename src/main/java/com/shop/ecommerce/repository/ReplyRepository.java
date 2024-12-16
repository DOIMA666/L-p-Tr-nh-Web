package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.ReplyEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {


    ReplyEntity findByFeedbackEntity_IdAndStatus(Long id, Integer status);

}
