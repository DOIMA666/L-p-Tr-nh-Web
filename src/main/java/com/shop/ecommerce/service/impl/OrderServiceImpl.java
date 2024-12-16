package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.OrderDetailEntity;
import com.shop.ecommerce.entity.OrderEntity;
import com.shop.ecommerce.enums.OrderStatusEnum;
import com.shop.ecommerce.payload.data.FinanceData;
import com.shop.ecommerce.payload.dto.OrderDto;
import com.shop.ecommerce.payload.request.OrderFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.repository.OrderDetailRepository;
import com.shop.ecommerce.repository.OrderRepository;
import com.shop.ecommerce.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        OrderEntity order = orderRepository.findById(id).get();
        if(status == 1) {
            order.setOrderStatusEnum(OrderStatusEnum.PENDING);
        }else if(status == 0) {
            order.setOrderStatusEnum(OrderStatusEnum.CANCELED);
        }
        else if(status == 2) {
            order.setOrderStatusEnum(OrderStatusEnum.SUCCEEDED);
        }
        orderRepository.save(order);
    }

    @Override
    public OrderEntity saveOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    @Override
    public void saveAllOrders(Set<OrderDetailEntity> orderDetailEntities) {
        orderDetailRepository.saveAll(orderDetailEntities);
    }

    @Override
    public Long countOrders() {
        return orderRepository.countByStatus(1);
    }

    @Override
    public OrderEntity findEntityById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public List<OrderEntity> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public BaseResponse<Page<OrderDto>> getAllByFilter(OrderFilterRequest filterRequest, Long id,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> orderEntities = orderRepository.findAllByFilter(filterRequest, id, pageable);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

        List<OrderDto> orderDtos = orderEntities.getContent().stream().map(orderEntity -> {
            System.out.println(orderEntity);
            OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);

            LocalDateTime createdDate = orderEntity.getCreatedAt();
            String day = createdDate.format(dayFormatter);
            String month = createdDate.format(monthFormatter);

            orderDto.setDay(day);
            orderDto.setMonth(month);
            orderDto.setDetailCount(orderEntity.getOrderDetailEntities().size());
            if(orderEntity.getOrderStatusEnum() != null) {
                orderDto.setOrderStatus(orderEntity.getOrderStatusEnum().name());
            }
            else orderDto.setOrderStatus("00");
            orderDto.setUserId(orderEntity.getCustomerEntity().getUserEntity().getId());
            orderDto.setEmail(orderEntity.getCustomerEntity().getUserEntity().getEmail());

            return orderDto;
        }).collect(Collectors.toList());

        Page<OrderDto> pageData = new PageImpl(orderDtos, pageable, orderEntities.getTotalElements());
        BaseResponse<Page<OrderDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }

    @Override
    public BaseResponse<Page<OrderDto>> getAllByFilterAdmin(OrderFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> orderEntities = orderRepository.findAllByFilterAdmin(filterRequest, pageable);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<OrderDto> orderDtos = orderEntities.getContent().stream().map(orderEntity -> {
            OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);

            LocalDateTime createdDate = orderEntity.getCreatedAt();
            String day = createdDate.format(dayFormatter);
            String month = createdDate.format(monthFormatter);

            orderDto.setDay(day);
            orderDto.setMonth(month);
            String formattedDate = orderEntity.getCreatedAt().format(formatter);
            orderDto.setCreatedDate(formattedDate);

            if(orderEntity.getOrderStatusEnum().name().equals("PENDING")) {
                orderDto.setStatus(1);
            }
            else if(orderEntity.getOrderStatusEnum().name().equals("CANCELED")) {
                orderDto.setStatus(0);
            }
            else if(orderEntity.getOrderStatusEnum().name().equals("SUCCEEDED")) {
                orderDto.setStatus(2);
            }
            orderDto.setDetailCount(orderEntity.getOrderDetailEntities().size());

            orderDto.setUserId(orderEntity.getCustomerEntity().getUserEntity().getId());
            orderDto.setEmail(orderEntity.getCustomerEntity().getUserEntity().getEmail());

            return orderDto;
        }).collect(Collectors.toList());

        Page<OrderDto> pageData = new PageImpl(orderDtos, pageable, orderEntities.getTotalElements());
        BaseResponse<Page<OrderDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }

    @Override
    public List<FinanceData> getFinanceDataByMonthAndYear() {
        List<Object[]> results = orderRepository.getFinanceDataByDayInCurrentMonth();
        List<FinanceData> financeDataList = new ArrayList<>();

        for (Object[] result : results) {
            BigDecimal total = (BigDecimal) result[0];
            Integer day = (Integer) result[1];
            Integer month = (Integer) result[2];
            Integer year = (Integer) result[3];
            financeDataList.add(new FinanceData(total, year, month, day));
        }

        return financeDataList;
    }

    @Override
    public Double calculateAverageOrderCost() {
        return orderRepository.calculateAverageOrderCostByCurrentMonth();
    }

    @Override
    public BigDecimal calculateTotalOrderCost() {
        return orderRepository.calculateTotalOrderCostByCurrentMonth();
    }
}
