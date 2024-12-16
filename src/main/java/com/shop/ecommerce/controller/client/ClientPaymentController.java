package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.config.VNPayConfig;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.enums.OrderStatusEnum;
import com.shop.ecommerce.payload.dto.CustomerDto;
import com.shop.ecommerce.payload.wrapper.CartDetailWrapper;
import com.shop.ecommerce.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/client/payment")
public class ClientPaymentController {
    private CartDetailWrapper cartDetailWrapper;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartDetailService cartDetailService;
    private final AddressService addressService;
    private Long subtotal;
    private Long total;
    private Boolean orderSuccess;
    private String districtName;
    private String provinceName;
    private String wardName;
    private String detail;
    private String transactionNo;
    private String orderInfo;
    private String transactionStatus;

    public ClientPaymentController(CustomerService customerService, OrderService orderService, ProductService productService, CartDetailService cartDetailService, AddressService addressService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.productService = productService;
        this.cartDetailService = cartDetailService;
        this.addressService = addressService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model,
                           @RequestParam String options){
        this.subtotal = 0L;
        this.total = 0L;
        this.orderSuccess = false;
        this.provinceName = "";
        this.wardName = "";
        this.detail = "";
        this.transactionNo = "";
        this.districtName = "";
        this.orderInfo = "";
        this.transactionStatus = "";

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerDto customer = customerService.getProfile(email);

        String[] optionArray = options.split(",");
        List<String> listIdQuantityString = Arrays.asList(optionArray);
        List<String> listId = new ArrayList<>();
        List<Integer> listQuantity = new ArrayList<>();

        for (String option : listIdQuantityString) {
            String[] idQuantityArray = option.split("-");
            listId.add(idQuantityArray[0]);
            listQuantity.add(Integer.parseInt(idQuantityArray[1]));
        }

        cartDetailWrapper = cartDetailService.findAllItemsById(listId);
        Long subtotal = 0L;
        for (int i = 0; i < cartDetailWrapper.getCartDetailEntities().size(); i++) {
            CartDetailEntity c = cartDetailWrapper.getCartDetailEntities().get(i);
            c.setQuantity(listQuantity.get(i));
            subtotal += (long) c.getPriceOfOne().intValue() * c.getQuantity();
        }

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("products", cartDetailWrapper);
        model.addAttribute("options",  options);
        model.addAttribute("email",  email);
        model.addAttribute("customer",  customer);
        model.addAttribute("idKhachHang", customer.getId());
        return "client/checkout";
    }


    @GetMapping("/pay")
    public String getPay(@RequestParam("totalResult") Long totalResult, @RequestParam("subtotalResult") Long subtotalResult,
                         @RequestParam("saveProvince") String saveProvince,
                         @RequestParam("saveDistrict") String saveDistrict,
                         @RequestParam("saveWard") String saveWard,
                         @RequestParam("detail") String detail) throws UnsupportedEncodingException{
        this.subtotal = subtotalResult;
        this.total = totalResult;
        this.provinceName = saveProvince;
        this.districtName = saveDistrict;
        this.wardName = saveWard;
        this.detail = detail;

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String bankCode = "NCB";

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalResult * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        System.out.println(paymentUrl);

        return "redirect:" + paymentUrl;
    }

    @GetMapping("/getPaymentInfo")
    public String getInfo(@RequestParam("vnp_TransactionNo") String transactionNo,
                          @RequestParam("vnp_OrderInfo") String orderInfo,
                          @RequestParam("vnp_TransactionStatus") String transactionStatus) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setProvince(provinceName);
        addressEntity.setDistrict(districtName);
        addressEntity.setWard(wardName);
        addressEntity.setDetails(detail);
        addressEntity.setCreatedAt(LocalDateTime.now());
        addressEntity.setCreatedBy(email);
        addressEntity.setUpdatedBy(email);
        addressEntity.setUpdatedAt(LocalDateTime.now());

        AddressEntity saveAddress = addressService.saveAddress(addressEntity);

        this.transactionStatus = transactionStatus;
        this.transactionNo = transactionNo;
        this.orderInfo = orderInfo;
        if(!Objects.equals(transactionNo, "0")) {
            OrderEntity order = new OrderEntity();
            order.setAddressEntity(saveAddress);
            order.setOrderTime(LocalDateTime.now());
            order.setFullCost(BigDecimal.valueOf(subtotal));
            order.setTotalCost(BigDecimal.valueOf(total));
            order.setStatus(1);
            order.setOrderInfo(orderInfo);
            order.setTransactionStatus(transactionStatus);
            order.setOrderStatusEnum(OrderStatusEnum.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            CustomerEntity customerEntity = customerService.findByEmail(email);
            order.setCreatedBy(email);
            order.setUpdatedBy(email);
            order.setCustomerEntity(customerEntity);
            order = orderService.saveOrder(order);

            Set<OrderDetailEntity> orderDetailEntities = new HashSet<>();

            for(CartDetailEntity c : cartDetailWrapper.getCartDetailEntities()) {
                ProductEntity productEntity = c.getProductEntity();
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setQuantity(c.getQuantity());
                orderDetailEntity.setPriceOfOne(c.getPriceOfOne());
                orderDetailEntity.setTotalPrice(c.getTotalPrice());
                orderDetailEntity.setCreatedAt(LocalDateTime.now());
                orderDetailEntity.setUpdatedAt(LocalDateTime.now());
                orderDetailEntity.setCreatedBy(email);
                orderDetailEntity.setUpdatedBy(email);

                orderDetailEntity.setStatus(1);
                orderDetailEntity.setOrderEntity(order);
                productEntity.setAmount(productEntity.getAmount() - c.getQuantity());
                productEntity = productService.save(productEntity);
                orderDetailEntity.setProductEntity(productEntity);

                orderDetailEntities.add(orderDetailEntity);
                cartDetailService.deleteInCart(c);
            }
            orderService.saveAllOrders(orderDetailEntities);
            this.orderSuccess = true;
        }
        else {
            OrderEntity order = new OrderEntity();
            order.setAddressEntity(saveAddress);
            order.setOrderTime(LocalDateTime.now());
            order.setFullCost(BigDecimal.valueOf(subtotal));
            order.setTotalCost(BigDecimal.valueOf(total));
            order.setStatus(1);
            order.setOrderInfo(orderInfo);
            order.setOrderStatusEnum(OrderStatusEnum.CANCELED);
            order.setTransactionStatus(transactionStatus);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            CustomerEntity customerEntity = customerService.findByEmail(email);
            order.setCreatedBy(email);
            order.setUpdatedBy(email);
            order.setCustomerEntity(customerEntity);
            orderService.saveOrder(order);
            Set<OrderDetailEntity> orderDetailEntities = new HashSet<>();

            for(CartDetailEntity c : cartDetailWrapper.getCartDetailEntities()) {
                ProductEntity productEntity = c.getProductEntity();
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setQuantity(c.getQuantity());
                orderDetailEntity.setPriceOfOne(c.getPriceOfOne());
                orderDetailEntity.setTotalPrice(c.getTotalPrice());
                orderDetailEntity.setCreatedAt(LocalDateTime.now());
                orderDetailEntity.setUpdatedAt(LocalDateTime.now());
                orderDetailEntity.setCreatedBy(email);
                orderDetailEntity.setUpdatedBy(email);

                orderDetailEntity.setStatus(1);
                orderDetailEntity.setOrderEntity(order);
                orderDetailEntity.setProductEntity(productEntity);

                orderDetailEntities.add(orderDetailEntity);
                cartDetailService.deleteInCart(c);
            }
            orderService.saveAllOrders(orderDetailEntities);
            this.orderSuccess = false;

        }
        return "redirect:/client/payment/after-checkout";
    }
    @GetMapping("/after-checkout")
    public String after(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerEntity customerEntity = customerService.findByEmail(email);
        model.addAttribute("email", email);
        model.addAttribute("orderSuccess", orderSuccess);
        model.addAttribute("province", provinceName);
        model.addAttribute("district", districtName);
        model.addAttribute("ward", wardName);
        model.addAttribute("detail", detail);
        model.addAttribute("transactionStatus", transactionStatus);
        model.addAttribute("transactionNo", transactionNo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("customer", customerEntity);
        model.addAttribute("total", total);
        return "client/after-checkout";
    }
}
