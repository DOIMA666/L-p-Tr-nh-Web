package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.service.CartDetailService;
import com.shop.ecommerce.service.CartService;
import com.shop.ecommerce.service.CustomerService;
import com.shop.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/client/cart")
public class ClientCartController {
    private final ProductService productService;
    private final CustomerService customerService;
    private final CartDetailService cartDetailService;
    private final CartService cartService;


    public ClientCartController(ProductService productService, CustomerService customerService, CartDetailService cartDetailService, CartService cartService) {
        this.productService = productService;
        this.customerService = customerService;
        this.cartDetailService = cartDetailService;
        this.cartService = cartService;
    }

    @GetMapping("")
    public String cart(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!Objects.equals(email, "")) {
            model.addAttribute("products", cartService.getAllDetails(email));
            model.addAttribute("email", email);
            return "client/cart";
        }
        else {
            return "redirect:/login";
        }
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") Long productId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.addToCart(productId, email);
        return "redirect:/client/cart";
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestParam Long idGioHangChiTiet, @RequestParam Integer soLuong) {
        try {
            cartDetailService.updateDetail(idGioHangChiTiet, soLuong);
            return ResponseEntity.ok("Update successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating quantity: " + e.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCartDetail(@PathVariable("id") Long id){
        cartDetailService.deleteById(id);
        return "redirect:/client/cart";
    }


}
