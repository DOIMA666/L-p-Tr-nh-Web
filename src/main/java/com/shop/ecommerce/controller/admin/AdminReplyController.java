package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.payload.dto.FeedbackDto;
import com.shop.ecommerce.payload.request.ReplyRequest;
import com.shop.ecommerce.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/reply")
public class AdminReplyController {
    private final ReplyService replyService;
    private final ProductService productService;
    private final ProductImageService productImageService;

    private final FeedbackService feedbackService;
    private final UserService userService;

    public AdminReplyController(ReplyService replyService, ProductService productService, ProductImageService productImageService, FeedbackService feedbackService, UserService userService) {
        this.replyService = replyService;
        this.productService = productService;
        this.productImageService = productImageService;
        this.feedbackService = feedbackService;
        this.userService = userService;
    }


    @PostMapping("/send")
    public String sendReply(Model model, @RequestParam("commentId") Long commentId, @RequestParam("productIdAddReply") Long productId, @RequestParam("addReplyContent")String addReplyContent) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ReplyRequest replyRequest = new ReplyRequest();
        replyRequest.setContent(addReplyContent);
        replyRequest.setFeedbackId(commentId);
        replyRequest.setEmail(email);
        replyService.processReply(replyRequest);

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

        return "redirect:/admin/product/detail/" + productId;
    }

    @PostMapping("/update")
    public String updateReply(Model model, @RequestParam("replyId") Long replyId, @RequestParam("productIdEditReply") Long productId, @RequestParam("editedReplyContent")String editedReplyContent) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ReplyRequest replyRequest = new ReplyRequest();
        replyRequest.setContent(editedReplyContent);
        replyRequest.setId(replyId);
        replyRequest.setEmail(email);
        replyService.updateReply(replyRequest);

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

        return "redirect:/admin/product/detail/" + productId;
    }

    @GetMapping("/delete/{id}")
    public String deleteReply(Model model, @PathVariable("id") Long replyId, @RequestParam("productId") Long productId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        replyService.deleteReply(replyId, email);
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

        return "redirect:/admin/product/detail/" + productId;
    }

}
