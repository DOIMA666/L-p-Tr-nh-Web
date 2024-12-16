package com.shop.ecommerce.controller.client;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.payload.dto.FeedbackDto;
import com.shop.ecommerce.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/client/feedback")
public class ClientFeedbackController {
    private final ProductService productService;
    private final UserService userService;
    private final ProductImageService productImageService;
    private final FeedbackService feedbackService;

    public ClientFeedbackController(ProductService productService, CustomerService customerService, UserService userService, ProductImageService productImageService, FeedbackService feedbackService) {
        this.productService = productService;
        this.userService = userService;
        this.productImageService = productImageService;
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add")
    public String addFeedback(@RequestParam("productId") Long productId, @ModelAttribute("feedback")FeedbackDto feedbackDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        feedbackDto.setProductId(productId);
        feedbackService.addFeedback(feedbackDto, email);
        return "redirect:/client/product/detail/" + productId;
    }
    @PostMapping("/update")
    public String updateFeedback(@RequestParam("editedProductId") Long productId,
                                 @RequestParam("editedCommentId") Long editedCommentId,
                                 @RequestParam("editedStar") Integer editedStar,
                                 @RequestParam("editedComment") String editedComment,
                                 Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setId(editedCommentId);
        feedbackDto.setRatedStar(editedStar);
        feedbackDto.setComment(editedComment);
        feedbackDto.setProductId(productId);
        feedbackService.updateFeedback(feedbackDto, email);

        model.addAttribute("email", email);
        ProductEntity productEntity = productService.findEntityById(productId);
        model.addAttribute("product", productEntity);
        List<ProductImageEntity> images = productImageService.findByProductId(productId);
        model.addAttribute("imageEntities", images);
        List<FeedbackDto> feedbackDtos = feedbackService.getAllFeedbackOfProduct(productId);
        model.addAttribute("comments", feedbackDtos);
        feedbackDto = new FeedbackDto();
        model.addAttribute("feedback", feedbackDto);
        Long id = userService.findIdByEmail(email);
        model.addAttribute("countComments", feedbackService.countComments(productId));
        model.addAttribute("productId", productId);
        model.addAttribute("userId", id);
        return "redirect:/client/product/detail/" + productId;
    }

    @GetMapping("/delete/{productId}/{id}")
    public String deleteFeedback(@PathVariable("productId") Long productId, @PathVariable("id") Long commentId, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        feedbackService.deleteFeedback(commentId, email);

        model.addAttribute("email", email);
        ProductEntity productEntity = productService.findEntityById(productId);
        model.addAttribute("product", productEntity);
        List<ProductImageEntity> images = productImageService.findByProductId(productId);
        model.addAttribute("imageEntities", images);
        List<FeedbackDto> feedbackDtos = feedbackService.getAllFeedbackOfProduct(productId);
        model.addAttribute("comments", feedbackDtos);
        FeedbackDto feedbackDto = new FeedbackDto();
        model.addAttribute("feedback", feedbackDto);
        Long id = userService.findIdByEmail(email);
        model.addAttribute("countComments", feedbackService.countComments(productId));
        model.addAttribute("productId", productId);
        model.addAttribute("userId", id);
        return "redirect:/client/product/detail/" + productId;
    }

}
