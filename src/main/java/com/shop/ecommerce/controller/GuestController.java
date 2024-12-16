package com.shop.ecommerce.controller;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.service.CategoryService;
import com.shop.ecommerce.service.ProductImageService;
import com.shop.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class GuestController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final CategoryService categoryService;

    public GuestController(ProductService productService, ProductImageService productImageService, CategoryService categoryService) {
        this.productService = productService;
        this.productImageService = productImageService;
        this.categoryService = categoryService;
    }

    @GetMapping("/product")
    public String home(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        return "guest/view-products";
    }

    @GetMapping("/product/detail/{productId}")
    public String detailProductGuest(@PathVariable(name = "productId") Long productId, Model model) {
        ProductEntity productEntity = productService.findEntityById(productId);
        model.addAttribute("product", productEntity);
        List<ProductImageEntity> images = productImageService.findByProductId(productId);
        model.addAttribute("imageEntities", images);
        return "guest/product-details";
    }
}
