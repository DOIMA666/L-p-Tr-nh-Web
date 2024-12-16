package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.payload.dto.FeedbackDto;
import com.shop.ecommerce.payload.dto.ProductDto;
import com.shop.ecommerce.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    private final ProductImageService productImageService;

    private final FeedbackService feedbackService;
    private final CustomerService customerService;
    private final UserService userService;

    public AdminProductController(ProductService productService, CategoryService categoryService, ImageService imageService, ProductImageService productImageService, FeedbackService feedbackService, CustomerService customerService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.productImageService = productImageService;
        this.feedbackService = feedbackService;
        this.customerService = customerService;
        this.userService = userService;
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
        model.addAttribute("comments", feedbackDtos);
        Long id = userService.findIdByEmail(email);
        model.addAttribute("countComments", feedbackService.countComments(productId));
        model.addAttribute("productId", productId);
        model.addAttribute("userId", id);
        return "admin/reply";
    }

    @GetMapping("")
    public String productPage(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        List<ProductDto> productDtos = productService.getAll();
        model.addAttribute("products", productDtos);
        return "admin/product/list-product";
    }
    @GetMapping("/insertProductPage")
    public String insertProductPage(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        ProductDto productDto = new ProductDto();
        model.addAttribute("product", productDto);
        return "admin/product/insert-product";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute(name = "product") ProductDto productDto, @RequestParam(value = "fileThumbnail", required = false) MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        productService.save(productDto, username, file);
        return "redirect:/admin/product";
    }

    @GetMapping("/updateProduct/{id}")
    public String getFormUpdateProduct(@PathVariable("id") Long id, Model model){
        model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
        ProductDto productDto = productService.findById(id);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("product", productDto);
        model.addAttribute("id", productDto.getId());
        return "admin/product/update-product";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute(name = "product") ProductDto productDto, @RequestParam(value = "fileThumbnail", required = false) MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(productDto);
        productService.update(productDto, username, file);
        return "redirect:/admin/product/updateProduct/" + productDto.getId();
    }

    @GetMapping("/deleteProduct/{id}")
    public String delete(@PathVariable("id") Long id, Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        productService.deleteById(id, username);
        return "redirect:/admin/product";
    }

    @GetMapping("deleteImage/{productId}/{imageId}")
    public String deleteImage(@PathVariable Long productId, @PathVariable Long imageId) {
        productService.deleteProductImage(imageId);
        return "redirect:/admin/product/updateProduct/" + productId;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("idInput") Long id, MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("file");
        String content = request.getParameter("content");
        imageService.uploadUpdate(id, file, content, SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/admin/product/updateProduct/" + id;
    }
}