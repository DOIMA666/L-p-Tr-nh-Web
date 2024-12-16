package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.FeedbackEntity;
import com.shop.ecommerce.entity.ReplyEntity;
import com.shop.ecommerce.entity.UserEntity;
import com.shop.ecommerce.payload.request.ReplyRequest;
import com.shop.ecommerce.repository.FeedbackRepository;
import com.shop.ecommerce.repository.ReplyRepository;
import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.service.ReplyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReplyServiceImpl(ReplyRepository replyRepository, FeedbackRepository feedbackRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.replyRepository = replyRepository;
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void processReply(ReplyRequest replyRequest) {
        FeedbackEntity feedbackEntity = feedbackRepository.findById(replyRequest.getFeedbackId()).get();
        UserEntity userEntity = userRepository.findByEmailAndStatus(replyRequest.getEmail(), 1).get();
        if(feedbackEntity.getReplyEntity() == null) {
            ReplyEntity replyEntity = new ReplyEntity();
            replyEntity.setCreatedAt(LocalDateTime.now());
            replyEntity.setUpdatedAt(LocalDateTime.now());
            replyEntity.setCreatedBy(replyRequest.getEmail());
            replyEntity.setUpdatedBy(replyRequest.getEmail());

            replyEntity.setContent(replyRequest.getContent());
            replyEntity.setFeedbackEntity(feedbackEntity);
            replyEntity.setStatus(1);
            replyEntity.setUserEntity(userEntity);
            replyRepository.save(replyEntity);
        }
    }

    @Override
    public void updateReply(ReplyRequest replyRequest) {
        ReplyEntity replyEntity = replyRepository.findById(replyRequest.getId()).get();
        replyEntity.setUpdatedAt(LocalDateTime.now());
        replyEntity.setUpdatedBy(replyRequest.getEmail());
        replyEntity.setContent(replyRequest.getContent());
        replyEntity.setStatus(1);
        replyRepository.save(replyEntity);
    }

    @Override
    public void deleteReply(Long id, String email) {
        ReplyEntity replyEntity = replyRepository.findById(id).get();
        replyEntity.setStatus(0);
        replyEntity.setCreatedBy(email);
        replyEntity.setUpdatedBy(email);
        replyRepository.save(replyEntity);
    }
}
