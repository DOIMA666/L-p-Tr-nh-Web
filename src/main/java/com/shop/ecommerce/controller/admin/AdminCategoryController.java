package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.payload.dto.CategoryDto;
import com.shop.ecommerce.service.CategoryService;
import com.shop.ecommerce.service.ProductService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminCategoryController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String category(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        return "admin/category/list-category";
    }
    @GetMapping("/insertCategoryPage")
    public String insertProductPage(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("category", categoryDto);
        return "admin/category/insert-category";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute(name = "category") CategoryDto categoryDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        categoryService.save(categoryDto, username);
        return "redirect:/admin/category";
    }

    @GetMapping("/updateCategory/{id}")
    public String getFormUpdateCategory(@PathVariable("id") Long id, Model model){
        model.addAttribute("email", SecurityContextHolder.getContext().getAuthentication().getName());
        CategoryDto categoryDto = categoryService.findDtoById(id);
        model.addAttribute("category", categoryDto);
        model.addAttribute("id", categoryDto.getId());
        return "admin/category/update-category";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute(name = "category") CategoryDto categoryDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        categoryService.update(categoryDto, username);
        return "redirect:/admin/category/updateCategory/" + categoryDto.getId();
    }

    @GetMapping("/deleteCategory/{id}")
    public String delete(@PathVariable("id") Long id, Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        categoryService.deleteById(id, username);
        return "redirect:/admin/category";
    }
}