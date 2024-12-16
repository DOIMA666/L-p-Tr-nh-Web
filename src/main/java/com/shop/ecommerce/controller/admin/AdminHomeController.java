package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.entity.UserEntity;
import com.shop.ecommerce.payload.dto.CustomerDto;
import com.shop.ecommerce.payload.dto.UserDto;
import com.shop.ecommerce.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    private final ProductService productService;
    private final FeedbackService feedbackService;
    private final OrderService orderService;
    private final UserService userService;
    private final CustomerService customerService;

    public AdminHomeController(ProductService productService, FeedbackService feedbackService, OrderService orderService, UserService userService, CustomerService customerService) {
        this.productService = productService;
        this.feedbackService = feedbackService;
        this.orderService = orderService;
        this.userService = userService;
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        model.addAttribute("countProducts", productService.countProductActive());
        model.addAttribute("countOrders", orderService.countOrders());
        model.addAttribute("countCustomers", customerService.countCustomers());
        model.addAttribute("feedbacks", feedbackService.getTop4());
        model.addAttribute("average", orderService.calculateAverageOrderCost());
        model.addAttribute("total", orderService.calculateTotalOrderCost());
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String adminProfile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);

        UserDto userDto = userService.getProfile(email);
        model.addAttribute("user", userDto);
        return "admin/profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfileAdmin(@ModelAttribute(name = "user")UserDto userDto, @RequestParam("file") MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateProfile(userDto, file, email);
        return "redirect:/admin/profile";
    }
}