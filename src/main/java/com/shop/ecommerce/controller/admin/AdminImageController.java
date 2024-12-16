package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.payload.dto.ProductDto;
import com.shop.ecommerce.service.CategoryService;
import com.shop.ecommerce.service.ImageService;
import com.shop.ecommerce.service.ProductService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/image")
public class AdminImageController {
    private final ImageService imageService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminImageController(ImageService imageService, ProductService productService, CategoryService categoryService) {
        this.imageService = imageService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/update")
    public String updateReply(Model model, @RequestParam("imageId") Long imageId, @RequestParam("productIdEditDesc") Long productId, @RequestParam("editedContent")String editedContent) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        imageService.updateDescription(imageId, editedContent, email);

        model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
        ProductDto productDto = productService.findById(productId);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("product", productDto);
        model.addAttribute("id", productDto.getId());

        return "redirect:/admin/product/updateProduct/" + productId;
    }


}
