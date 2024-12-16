package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.payload.dto.FeedbackDto;
import com.shop.ecommerce.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/client/product")
public class ClientViewProductController {
    private final ProductService productService;
    private final CustomerService customerService;
    private final CategoryService categoryService;
    private final FeedbackService feedbackService;
    private final UserService userService;
    private final ProductImageService productImageService;

    public ClientViewProductController(ProductService productService, CustomerService customerService, CategoryService categoryService, FeedbackService feedbackService, UserService userService, ProductImageService productImageService) {
        this.productService = productService;
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.feedbackService = feedbackService;
        this.userService = userService;
        this.productImageService = productImageService;
    }

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("categories", categoryService.getAll());
        return "client/view-products";
    }

    @GetMapping("/detail/{productId}")
    public String detailProductGuest(@PathVariable(name = "productId") Long productId, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        ProductEntity productEntity = productService.findEntityById(productId);
        model.addAttribute("product", productEntity);
        List<ProductImageEntity> images = productImageService.findByProductId(productId);
        model.addAttribute("imageEntities", images);
        List<FeedbackDto> feedbackDtos = feedbackService.getAllFeedbackOfProduct(productId);
        System.out.println(feedbackDtos);
        model.addAttribute("comments", feedbackDtos);
        FeedbackDto feedbackDto = new FeedbackDto();
        model.addAttribute("feedback", feedbackDto);
        Long id = userService.findIdByEmail(email);
        model.addAttribute("countComments", feedbackService.countComments(productId));
        model.addAttribute("productId", productId);
        model.addAttribute("userId", id);
        model.addAttribute("canFeedback", customerService.checkExistOrder(id, productId));
        return "client/product-details";
    }
}
