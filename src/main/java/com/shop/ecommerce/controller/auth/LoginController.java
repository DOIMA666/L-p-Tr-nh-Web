package com.shop.ecommerce.controller.auth;

import com.shop.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        boolean isValidCredentials = userService.validateCredentials(username, password);
        if (isValidCredentials) {
            return "redirect:/index";
        } else {
            model.addAttribute("error", "Sai email hoặc mật khẩu!");
            return "auth/login";
        }
    }
    @GetMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("error", "Sai email hoặc mật khẩu!");
        return "auth/loginError";
    }

    @GetMapping("/loginInactive")
    public String loginInactive() {
        return "auth/loginInactive";
    }
}

