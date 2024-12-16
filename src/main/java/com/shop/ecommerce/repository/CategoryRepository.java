package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.CategoryEntity;
import com.shop.ecommerce.payload.request.CategoryFilterRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SpringBootApplication
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(value = "SELECT c FROM CategoryEntity c where c.status = 1")
    List<CategoryEntity> getAll();

    @Query(value = "SELECT c FROM CategoryEntity c " +
            "WHERE " +
            "(:#{#condition.name} IS NULL OR LOWER(c.categoryName) LIKE '%' || LOWER(:#{#condition.name}) || '%') " +
            "AND (:#{#condition.status} IS NULL OR c.status = :#{#condition.status}) "
    )
    Page<CategoryEntity> findAllByFilter(@Param("condition") CategoryFilterRequest filterRequest, Pageable pageable);


}
