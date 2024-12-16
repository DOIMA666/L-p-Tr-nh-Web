package com.shop.ecommerce.controller.resource;

import com.shop.ecommerce.payload.dto.CategoryDto;
import com.shop.ecommerce.payload.request.CategoryFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class CategoryResource {

    private final CategoryService service;
    public CategoryResource(CategoryService service) {
        this.service = service;
    }
    @PostMapping("/categoryList")
    public ResponseEntity<BaseResponse<Page<CategoryDto>>> getAll(@RequestBody CategoryFilterRequest filterRequest,
                                                                  @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                                  @RequestParam(name = "size",required = false,defaultValue = "10") int size){
        return ResponseEntity.ok(service.getAllByFilter(filterRequest,page,size));
    }
}
