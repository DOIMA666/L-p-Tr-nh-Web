package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String productPage(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        return "admin/user/list-user";
    }

    @PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<String> updateUserStatus(@RequestParam Long userId, @RequestParam Integer newStatus) {
        System.out.println(userId + " " + newStatus);
        try {
            userService.updateUserStatus(userId, newStatus);

            return ResponseEntity.ok("Update successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating status: " + e.getMessage());
        }
    }

}