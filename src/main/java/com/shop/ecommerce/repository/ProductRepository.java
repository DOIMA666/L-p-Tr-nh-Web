package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.payload.request.ProductFilterRequest;
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
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByStatus(Integer status);

    @Query(value = "SELECT p FROM ProductEntity p where p.status = 1 ORDER BY p.id ASC LIMIT 4")
    List<ProductEntity> getTop4();

    Long countByStatus(Integer status);

    @Query(value = "SELECT p FROM ProductEntity p " +
            "LEFT JOIN p.categoryEntity c " +
            "WHERE " +
            "(:#{#condition.productName} IS NULL OR LOWER(p.productName) LIKE '%' || LOWER(:#{#condition.productName}) || '%') " +
            "AND (:#{#condition.status} IS NULL OR p.status = :#{#condition.status}) " +
            "AND (:#{#condition.startPrice} = 0 OR :#{#condition.endPrice} = 0 OR :#{#condition.startPrice} IS NULL OR :#{#condition.endPrice} IS NULL OR (p.price between :#{#condition.startPrice} AND :#{#condition.endPrice})) " +
            "AND (:#{#condition.saleStartPrice} = 0 OR :#{#condition.saleEndPrice} = 0 OR :#{#condition.saleStartPrice} IS NULL OR :#{#condition.saleEndPrice} IS NULL OR (p.salePrice between :#{#condition.saleStartPrice} AND :#{#condition.saleEndPrice})) " +
            "AND (:#{#condition.status} IS NULL OR p.status = :#{#condition.status}) " +
            "AND (:#{#condition.categoryId} IS NULL OR :#{#condition.categoryId} = 0 OR c.id = :#{#condition.categoryId})"
    )
    Page<ProductEntity> findAllByFilter(@Param("condition") ProductFilterRequest filterRequest, Pageable pageable);

    @Query(value = "SELECT p FROM ProductEntity p " +
            "LEFT JOIN p.categoryEntity c " +
            "WHERE " +
            "p.status = 1 " +
            "AND (:#{#condition.productName} IS NULL OR LOWER(p.productName) LIKE '%' || LOWER(:#{#condition.productName}) || '%') " +
            "AND (:#{#condition.saleStartPrice} IS NULL OR :#{#condition.saleEndPrice} IS NULL OR (p.salePrice BETWEEN :#{#condition.saleStartPrice} AND :#{#condition.saleEndPrice})) " +
            "AND (:#{#condition.categoryId} IS NULL OR :#{#condition.categoryId} = 0 OR c.id = :#{#condition.categoryId})"
    )
    Page<ProductEntity> findAllInClientByFilter(@Param("condition") ProductFilterRequest filterRequest, Pageable pageable);


}
