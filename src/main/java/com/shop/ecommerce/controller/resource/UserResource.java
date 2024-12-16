package com.shop.ecommerce.controller.resource;

import com.shop.ecommerce.payload.dto.UserDto;
import com.shop.ecommerce.payload.request.UserFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userList")
public class UserResource {

    private final UserService service;
    public UserResource(UserService service) {
        this.service = service;
    }
    @PostMapping()
    public ResponseEntity<BaseResponse<Page<UserDto>>> getAll(@RequestBody UserFilterRequest filterRequest,
                                                              @RequestParam(name = "page",required = false,defaultValue = "0") int page,
                                                              @RequestParam(name = "size",required = false,defaultValue = "10") int size){
        return ResponseEntity.ok(service.getAllByFilter(filterRequest,page,size));
    }
}
