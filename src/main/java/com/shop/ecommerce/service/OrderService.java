package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.OrderDetailEntity;
import com.shop.ecommerce.entity.OrderEntity;
import com.shop.ecommerce.payload.data.FinanceData;
import com.shop.ecommerce.payload.dto.OrderDto;
import com.shop.ecommerce.payload.request.OrderFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public interface OrderService {
    OrderEntity saveOrder(OrderEntity order);
    void saveAllOrders(Set<OrderDetailEntity> orderDetailEntities);
    Long countOrders();
    OrderEntity findEntityById(Long id);

    List<OrderEntity> getAll();

    void updateOrderStatus(Long id, Integer status);
    BaseResponse<Page<OrderDto>> getAllByFilter(OrderFilterRequest filterRequest, Long id, int page, int size);
    BaseResponse<Page<OrderDto>> getAllByFilterAdmin(OrderFilterRequest filterRequest, int page, int size);
    List<FinanceData> getFinanceDataByMonthAndYear();

    Double calculateAverageOrderCost();
    BigDecimal calculateTotalOrderCost();
}

