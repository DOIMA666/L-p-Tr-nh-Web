package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.OrderEntity;
import com.shop.ecommerce.payload.request.OrderFilterRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@SpringBootApplication
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Long countByStatus(Integer status);
    @Query(value = "SELECT o FROM OrderEntity o " +
            "LEFT JOIN o.customerEntity c " +
            "WHERE c.id = :id " +
            "AND c.status = 1 " +
            "AND (:#{#condition.fullCost} IS NULL OR o.fullCost = :#{#condition.fullCost}) " +
            "AND (:#{#condition.status} IS NULL OR o.status = :#{#condition.status}) " +
            "AND (:#{#condition.createdDate} IS NULL OR o.orderTime = :#{#condition.createdDate}) "
    )
    Page<OrderEntity> findAllByFilter(@Param("condition") OrderFilterRequest filterRequest, @Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT o FROM OrderEntity o " +
            "LEFT JOIN o.customerEntity c " +
            "WHERE (:#{#condition.fullCost} IS NULL OR o.fullCost = :#{#condition.fullCost}) " +
            "AND (:#{#condition.status} IS NULL OR o.status = :#{#condition.status}) " +
            "AND (:#{#condition.createdDate} IS NULL OR o.orderTime = :#{#condition.createdDate}) "
    )
    Page<OrderEntity> findAllByFilterAdmin(@Param("condition") OrderFilterRequest filterRequest, Pageable pageable);

    @Query("SELECT SUM(o.totalCost), DAY(o.orderTime), MONTH(o.orderTime), YEAR (o.orderTime) FROM OrderEntity o WHERE YEAR(o.orderTime) = YEAR(CURRENT_DATE) AND MONTH(o.orderTime) = MONTH(CURRENT_DATE) GROUP BY DAY(o.orderTime), MONTH(o.orderTime), YEAR(o.orderTime)")
    List<Object[]> getFinanceDataByDayInCurrentMonth();

    @Query("SELECT AVG(o.totalCost) FROM OrderEntity o WHERE YEAR(o.orderTime) = YEAR(CURRENT_DATE) AND MONTH(o.orderTime) = MONTH(CURRENT_DATE)")
    Double calculateAverageOrderCostByCurrentMonth();

    @Query("SELECT SUM(o.totalCost) FROM OrderEntity o WHERE YEAR(o.orderTime) = YEAR(CURRENT_DATE) AND MONTH(o.orderTime) = MONTH(CURRENT_DATE)")
    BigDecimal calculateTotalOrderCostByCurrentMonth();

}
