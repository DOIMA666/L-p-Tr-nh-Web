package com.shop.ecommerce.controller.resource;

import com.shop.ecommerce.payload.dto.ProductDto;
import com.shop.ecommerce.payload.request.ProductFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class ProductResource {

    private final ProductService service;
    public ProductResource(ProductService service) {
        this.service = service;
    }
    @PostMapping("/productList")
    public ResponseEntity<BaseResponse<Page<ProductDto>>> getAll(@RequestBody ProductFilterRequest filterRequest,
                                                                 @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                                 @RequestParam(name = "size",required = false,defaultValue = "10") int size){
        Long start = filterRequest.getSaleStartPrice();
        Long end = filterRequest.getSaleEndPrice();

        if(start == null && end == null) {
            filterRequest.setSaleStartPrice(0L);
            filterRequest.setSaleEndPrice(0L);
        }
        else if(start == null) {
            filterRequest.setSaleStartPrice(0L);
            filterRequest.setSaleEndPrice(end * 1000);
        }
        else if(end == null) {
            filterRequest.setSaleStartPrice(start * 1000);
            filterRequest.setSaleEndPrice(0L);
        }
        else {
            filterRequest.setSaleStartPrice(start * 1000);
            filterRequest.setSaleEndPrice(end * 1000);
        }
        return ResponseEntity.ok(service.getAllByFilter(filterRequest,page,size));
    }

    @PostMapping("/productListClient")
    public ResponseEntity<BaseResponse<Page<ProductDto>>> getAllInClient(@RequestBody ProductFilterRequest filterRequest,
                                                                 @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                                 @RequestParam(name = "size",required = false,defaultValue = "12") int size){

        Long start = filterRequest.getSaleStartPrice();
        Long end = filterRequest.getSaleEndPrice();


        if(start == null && end == null) {
            filterRequest.setSaleStartPrice(null);
            filterRequest.setSaleEndPrice(null);
        }
        else if(start == null) {
            filterRequest.setSaleStartPrice(0L);
            filterRequest.setSaleEndPrice(end);
        }
        else if(end == null) {
            filterRequest.setSaleStartPrice(start);
            filterRequest.setSaleEndPrice(0L);
        }
        else {
            filterRequest.setSaleStartPrice(start);
            filterRequest.setSaleEndPrice(end);
        }

        System.out.println(filterRequest.getProductName());
        return ResponseEntity.ok(service.getAllInClientByFilter(filterRequest,page,size));
    }
}
