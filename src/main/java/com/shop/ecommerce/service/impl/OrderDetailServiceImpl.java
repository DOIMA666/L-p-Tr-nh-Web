package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.OrderDetailEntity;
import com.shop.ecommerce.payload.dto.OrderDetailDto;
import com.shop.ecommerce.payload.request.OrderDetailFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.repository.OrderDetailRepository;
import com.shop.ecommerce.repository.OrderRepository;
import com.shop.ecommerce.service.OrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;

    public OrderDetailServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public BaseResponse<Page<OrderDetailDto>> getAllByFilter(OrderDetailFilterRequest filterRequest, Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAllByFilter(filterRequest, id, pageable);

        List<OrderDetailDto> orderDetailDtos = orderDetailEntities.getContent().stream().map(orderDetailEntity -> {
            OrderDetailDto orderDetailDto = modelMapper.map(orderDetailEntity, OrderDetailDto.class);
            orderDetailDto.setProductId(orderDetailEntity.getProductEntity().getId());
            orderDetailDto.setProductName(orderDetailEntity.getProductEntity().getProductName());
            orderDetailDto.setThumbnail(orderDetailEntity.getProductEntity().getThumbnail());
            orderDetailDto.setDescription(orderDetailEntity.getProductEntity().getDescription());
            orderDetailDto.setCategoryName(orderDetailEntity.getProductEntity().getCategoryEntity().getCategoryName());

            return orderDetailDto;
        }).collect(Collectors.toList());

        Page<OrderDetailDto> pageData = new PageImpl(orderDetailDtos, pageable, orderDetailEntities.getTotalElements());
        BaseResponse<Page<OrderDetailDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }
}
