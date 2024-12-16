package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.entity.CustomerEntity;
import com.shop.ecommerce.entity.OrderEntity;
import com.shop.ecommerce.service.CustomerService;
import com.shop.ecommerce.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/order")
public class ClientViewOrderController {
    private final OrderService orderService;
    private final CustomerService customerService;

    public ClientViewOrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }


    @GetMapping("")
    public String order(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        model.addAttribute("id", customerService.getIdByEmail(email));
        return "client/view-orders";
    }

    @GetMapping("/detail/{orderId}")
    public String detailOrder(@PathVariable(name = "orderId") Long orderId, Model model) {
        model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
        OrderEntity order = orderService.findEntityById(orderId);
        CustomerEntity customerEntity = order.getCustomerEntity();
        model.addAttribute("order", order);
        model.addAttribute("customer", customerEntity);
        return "client/order-details";
    }
}
