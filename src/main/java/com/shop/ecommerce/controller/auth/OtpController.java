package com.shop.ecommerce.controller.auth;

import com.shop.ecommerce.config.Constants;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.enums.GenderEnum;
import com.shop.ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Controller

public class OtpController {
    private final UserService userService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    Constants constants = new Constants();

    public OtpController(UserService userService, CartService cartService, CustomerService customerService, RoleService roleService, PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    @RequestMapping(value = "otp-check", method = RequestMethod.GET)
    public String indexOtp() {
        return "auth/otpConfirm";
    }

    @RequestMapping(value = "recover", method = RequestMethod.GET)
    public String recover() {
        return "auth/recoverPage";
    }

    @RequestMapping(value = "confirm-otp", method = RequestMethod.POST)
    public String checkOtp(HttpSession session, @RequestParam("otp") String otp, Model model) {
        String otpRegister = (String) session.getAttribute("otp-register");
        if (otp.equals(otpRegister)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail((String) session.getAttribute("email"));
            userEntity.setPassword(passwordEncoder.encode((String) session.getAttribute("password")));
            String phone = (String) session.getAttribute("phone");
            String fullName = (String) session.getAttribute("fullName");
            String address = (String) session.getAttribute("address");
            String gender = (String) session.getAttribute("gender");
            userEntity.setPhoneNumber(phone);
            userEntity.setStatus(1);

            RoleEntity client = roleService.findById(3L).get();

            Set<RoleEntity> roleEntities = new HashSet<>();

            roleEntities.add(client);
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setFullName(fullName);
            customerEntity.setStatus(1);
            customerEntity.setAddress(address);

            CartEntity cartEntity = new CartEntity();
            cartEntity.setTotalCost(BigDecimal.valueOf(0));
            cartEntity.setCreatedBy("SUPER_ADMIN");
            cartEntity.setUpdatedBy("SUPER_ADMIN");
            cartEntity.setCreatedAt(LocalDateTime.now());
            cartEntity.setUpdatedAt(LocalDateTime.now());
            cartEntity.setStatus(1);
            Set<CartDetailEntity> cartDetailEntities = new HashSet<>();
            cartEntity.setCartDetailEntities(cartDetailEntities);

            if(Objects.equals(gender, "1")) {
                customerEntity.setGender(GenderEnum.MALE);
            }
            else {
                customerEntity.setGender(GenderEnum.FEMALE);
            }
            userEntity.setRoleEntities(roleEntities);
            userEntity.setCreatedBy("SUPER_ADMIN");
            userEntity.setUpdatedBy("SUPER_ADMIN");
            customerEntity.setCreatedBy("SUPER_ADMIN");
            customerEntity.setUpdatedBy("SUPER_ADMIN");
            UserEntity user = userService.saveUser(userEntity);
            customerEntity.setUserEntity(user);
            customerEntity = customerService.saveEntity(customerEntity);
            cartEntity.setCustomerEntity(customerEntity);
            cartEntity = cartService.saveCart(cartEntity);

            customerEntity.setCartEntity(cartEntity);
            customerEntity = customerService.saveEntity(customerEntity);
            user.setCustomerEntity(customerEntity);
            userService.saveUser(user);
            return "redirect:/";
        }
        model.addAttribute("mess","OTP is not correct! Please check your email.");
        return "auth/otpConfirm";
    }

    @RequestMapping(value = "send-otp-recover", method = RequestMethod.POST)
    public String getMail( @RequestParam("emailaddress") String email, HttpSession session) {
        session.setAttribute("emailToReset",email);
        String otpCode = constants.otpCode();
        String subject = "Hello Here Is Your Code OTP!";
        String mess = "Hi You@" + " " + "Here is your OTP Code: " + otpCode + " Please input to form!" + "\n Thanks!";
        this.emailSenderService.sendEmail(email, subject, mess);
        session.setAttribute("recoverOtp",otpCode);
        session.setMaxInactiveInterval(360);
        return "auth/confirmOtpRecover";
    }

    @RequestMapping(value = "confirm-otp-recover", method = RequestMethod.POST)
    public String recover( @RequestParam("otp") String otp, Model model,HttpSession session) {
        if (session.getAttribute("recoverOtp").equals(otp)){
            return "auth/confirmNewPassword";
        }
        model.addAttribute("mess","OTP is not correct! Please check your email.");
        return "auth/confirmOtpRecover";
    }

    @RequestMapping(value = "save-new-password", method = RequestMethod.POST)
    public String saveNewPassword(@RequestParam("password") String password, HttpSession session) {
        String email = (String) session.getAttribute("emailToReset");
        if (userService.recoverPassword(passwordEncoder.encode(password),email)==1){
            return "redirect:/login";
        }
        return "auth/confirmNewPassword";
    }
}