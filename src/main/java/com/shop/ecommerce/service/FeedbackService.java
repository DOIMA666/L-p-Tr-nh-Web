package com.shop.ecommerce.service;

import com.shop.ecommerce.payload.dto.FeedbackDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService {
    List<FeedbackDto> getAllFeedbackOfProduct(Long productId);
    List<FeedbackDto> getTop4();
    void addFeedback(FeedbackDto feedbackDto, String email);
    Long countComments(Long productId);
    void updateFeedback(FeedbackDto feedbackDto, String email);
    void deleteFeedback(Long id, String email);
}

