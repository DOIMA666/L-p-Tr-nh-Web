package com.shop.ecommerce.controller.resource;

import com.shop.ecommerce.payload.data.FinanceData;
import com.shop.ecommerce.payload.dto.OrderDetailDto;
import com.shop.ecommerce.payload.dto.OrderDto;
import com.shop.ecommerce.payload.request.OrderDetailFilterRequest;
import com.shop.ecommerce.payload.request.OrderFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.service.OrderDetailService;
import com.shop.ecommerce.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class OrderResource {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public OrderResource(OrderService orderService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("/orderListAdmin")
    public ResponseEntity<BaseResponse<Page<OrderDto>>> getAllInAdmin(@RequestBody OrderFilterRequest filterRequest,
                                                               @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                               @RequestParam(name = "size",required = false,defaultValue = "10") int size){
        return ResponseEntity.ok(orderService.getAllByFilterAdmin(filterRequest, page, size));
    }

    @PostMapping("/orderList")
    public ResponseEntity<BaseResponse<Page<OrderDto>>> getAll(@RequestBody OrderFilterRequest filterRequest,
                                                               @RequestParam("id") Long id,
                                                               @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                               @RequestParam(name = "size",required = false,defaultValue = "10") int size){
        return ResponseEntity.ok(orderService.getAllByFilter(filterRequest, id, page,size));
    }

    @PostMapping("/orderDetailList")
    public ResponseEntity<BaseResponse<Page<OrderDetailDto>>> getAllDetails(@RequestBody OrderDetailFilterRequest filterRequest,
                                                                            @RequestParam("id") Long id,
                                                                            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                                            @RequestParam(name = "size",required = false,defaultValue = "10") int size){
        return ResponseEntity.ok(orderDetailService.getAllByFilter(filterRequest, id, page,size));
    }

    @GetMapping("/chart-data")
    public ResponseEntity<List<FinanceData>> getChartData() {
        List<FinanceData> financeDataList = orderService.getFinanceDataByMonthAndYear();
        return ResponseEntity.ok(financeDataList);
    }

}
