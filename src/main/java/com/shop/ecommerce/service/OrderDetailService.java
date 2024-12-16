package com.shop.ecommerce.service;

import com.shop.ecommerce.payload.dto.OrderDetailDto;
import com.shop.ecommerce.payload.request.OrderDetailFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface OrderDetailService {
    BaseResponse<Page<OrderDetailDto>> getAllByFilter(OrderDetailFilterRequest filterRequest, Long id, int page, int size);
}

