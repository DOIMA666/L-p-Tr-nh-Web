package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.service.ProductService;
import com.shop.ecommerce.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/client/home")
public class ClientHomeController {
    private final ProductService productService;
    private final UserService userService;

    public ClientHomeController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("")
    public String home(Model model) {
        List<ProductEntity> productEntities = productService.getTop4();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("products", productEntities);

        model.addAttribute("email", email);
        return "client/index";
    }



}
