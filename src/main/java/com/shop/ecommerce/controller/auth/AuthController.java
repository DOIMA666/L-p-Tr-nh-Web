package com.shop.ecommerce.controller.auth;

import com.shop.ecommerce.payload.dto.UserDto;
import com.shop.ecommerce.service.EmailSenderService;
import com.shop.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/?logout";
    }

    @RequestMapping(value = "register")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("user") UserDto user, @RequestParam(name = "role", defaultValue = "Student") String role, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("mess", "Email đã tồn tại. Hãy nhập Email mới!");
            return "auth/register";
        }
        session.setAttribute("otp-register", otpCode());
        session.setMaxInactiveInterval(360);
        String subject = "Hello Here Is Your Code OTP!";
        String mess = "Hi You@" + " \nDear " + user.getEmail() + "Here is your OTP Code: " + session.getAttribute("otp-register") + " Please input to form!" + "\n Thanks!";
        this.emailSenderService.sendEmail(user.getEmail(), subject, mess);
        session.setAttribute("email", user.getEmail());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("role", role);
        session.setAttribute("dob", user.getDob());
        session.setAttribute("phone", user.getPhone());
        session.setAttribute("gender", user.getGender());
        session.setAttribute("address", user.getAddress());
        session.setAttribute("password", user.getPassword());
        return "redirect:/otp-check";

    }

    @RequestMapping(value = "re-send")
    public String resend(HttpSession session) {
        session.removeAttribute("otp-register");
        session.setAttribute("otp-register", otpCode());
        session.setMaxInactiveInterval(360);
        String subject = "Hello Here Is Your Code OTP!";
        String mess = "Hi You@" + " \nDear " + session.getAttribute("email") + ", Here is your OTP Code: " + session.getAttribute("otp-register") + " Please input to form!" + "\n Thanks!";
        this.emailSenderService.sendEmail((String) session.getAttribute("email"), subject, mess);
        return "redirect:/otp-check";
    }

    public String otpCode() {
        int code = (int) Math.floor(((Math.random() * 899999) + 100000));
        return String.valueOf(code);
    }


}
