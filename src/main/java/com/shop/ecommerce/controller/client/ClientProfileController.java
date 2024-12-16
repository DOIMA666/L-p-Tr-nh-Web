package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.payload.dto.CustomerDto;
import com.shop.ecommerce.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@RequestMapping("/client/profile")
public class ClientProfileController {
    private final CustomerService customerService;

    public ClientProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public String profile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerDto customerDto = customerService.getProfile(email);
        System.out.println(customerDto.getPhoneNumber());
        model.addAttribute("customer", customerDto);
        return "client/profile";
    }
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute(name = "customer") CustomerDto customerDto, @RequestParam("file") MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(customerDto.getPhoneNumber());
        if(!Objects.equals(email, customerDto.getEmail())) {
            customerService.updateProfile(customerDto, file, email);
            return "redirect:/home";
        }
        else {
            customerService.updateProfile(customerDto, file, email);
            return "redirect:/client/profile";
        }
    }
}
