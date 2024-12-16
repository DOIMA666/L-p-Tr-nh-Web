package com.shop.ecommerce.service.impl;


import com.shop.ecommerce.entity.FeedbackEntity;
import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ReplyEntity;
import com.shop.ecommerce.entity.UserEntity;
import com.shop.ecommerce.payload.dto.FeedbackDto;
import com.shop.ecommerce.payload.dto.ReplyDto;
import com.shop.ecommerce.repository.FeedbackRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.repository.ReplyRepository;
import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.service.FeedbackService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ReplyRepository replyRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, ReplyRepository replyRepository, ProductRepository productRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.feedbackRepository = feedbackRepository;
        this.replyRepository = replyRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<FeedbackDto> getAllFeedbackOfProduct(Long productId) {
        List<FeedbackEntity> feedbackEntities = feedbackRepository.getAllByProductEntity_IdAndStatus(productId, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, uuuu 'at' h:mm a", Locale.ENGLISH);
        List<FeedbackDto> feedbackDtos = feedbackEntities.stream().map(feedbackEntity -> {
            FeedbackDto feedbackDto = modelMapper.map(feedbackEntity, FeedbackDto.class);
            ReplyEntity replyEntity = replyRepository.findByFeedbackEntity_IdAndStatus(feedbackEntity.getId(), 1);
            ReplyDto replyDto = null;
            String formattedDateTime;
            if(replyEntity != null) {
                replyDto = modelMapper.map(replyEntity, ReplyDto.class);
                replyDto.setFeedbackId(replyEntity.getFeedbackEntity().getId());
                replyDto.setProductId(feedbackEntity.getProductEntity().getId());
                replyDto.setUserId(replyEntity.getUserEntity().getId());
                formattedDateTime = replyEntity.getCreatedAt().format(formatter);
                replyDto.setCreatedDate(formattedDateTime);
                replyDto.setEmail(replyEntity.getUserEntity().getEmail());
                replyDto.setAvatarUser(replyEntity.getUserEntity().getAvatar().getImageLink());
            }
            feedbackDto.setReplyDto(replyDto);
            feedbackDto.setCustomerId(feedbackEntity.getCustomerEntity().getId());
            feedbackDto.setFullName(feedbackEntity.getCustomerEntity().getFullName());
            if (feedbackEntity.getCustomerEntity().getUserEntity().getAvatar() != null) {
                feedbackDto.setAvatar(feedbackEntity.getCustomerEntity().getUserEntity().getAvatar().getImageLink());
            } else {
                feedbackDto.setAvatar("");
            }
            formattedDateTime = feedbackEntity.getCreatedAt().format(formatter);
            feedbackDto.setCreatedDate(formattedDateTime);
            feedbackDto.setProductId(feedbackEntity.getProductEntity().getId());
            feedbackDto.setProductName(feedbackEntity.getProductEntity().getProductName());
            feedbackDto.setUserId(feedbackEntity.getCustomerEntity().getUserEntity().getId());
            feedbackDto.setEmail(feedbackEntity.getCustomerEntity().getUserEntity().getEmail());
            return feedbackDto;
        }).toList();
        return feedbackDtos;
    }

    @Override
    public List<FeedbackDto> getTop4() {
        List<FeedbackEntity> feedbackEntities = feedbackRepository.findTop4Feedback();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, uuuu 'at' h:mm a", Locale.ENGLISH);
        List<FeedbackDto> feedbackDtos = feedbackEntities.stream().map(feedbackEntity -> {
            FeedbackDto feedbackDto = modelMapper.map(feedbackEntity, FeedbackDto.class);
            ReplyEntity replyEntity = replyRepository.findByFeedbackEntity_IdAndStatus(feedbackEntity.getId(), 1);
            ReplyDto replyDto = null;
            String formattedDateTime;
            if(replyEntity != null) {
                replyDto = modelMapper.map(replyEntity, ReplyDto.class);
                replyDto.setFeedbackId(replyEntity.getFeedbackEntity().getId());
                replyDto.setProductId(feedbackEntity.getProductEntity().getId());
                replyDto.setUserId(replyEntity.getUserEntity().getId());
                formattedDateTime = replyEntity.getCreatedAt().format(formatter);
                replyDto.setCreatedDate(formattedDateTime);
                replyDto.setEmail(replyEntity.getUserEntity().getEmail());
                replyDto.setAvatarUser(replyEntity.getUserEntity().getAvatar().getImageLink());
            }
            feedbackDto.setReplyDto(replyDto);
            feedbackDto.setCustomerId(feedbackEntity.getCustomerEntity().getId());
            feedbackDto.setFullName(feedbackEntity.getCustomerEntity().getFullName());
            if (feedbackEntity.getCustomerEntity().getUserEntity().getAvatar() != null) {
                feedbackDto.setAvatar(feedbackEntity.getCustomerEntity().getUserEntity().getAvatar().getImageLink());
            } else {
                feedbackDto.setAvatar("");
            }
            formattedDateTime = feedbackEntity.getCreatedAt().format(formatter);
            feedbackDto.setCreatedDate(formattedDateTime);
            feedbackDto.setProductId(feedbackEntity.getProductEntity().getId());
            feedbackDto.setProductName(feedbackEntity.getProductEntity().getProductName());
            feedbackDto.setUserId(feedbackEntity.getCustomerEntity().getUserEntity().getId());
            feedbackDto.setEmail(feedbackEntity.getCustomerEntity().getUserEntity().getEmail());
            return feedbackDto;
        }).toList();
        return feedbackDtos;
    }

    @Override
    @Transactional
    public void addFeedback(FeedbackDto feedbackDto, String email) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        ProductEntity productEntity = productRepository.findById(feedbackDto.getProductId()).get();
        UserEntity userEntity = userRepository.findByEmailAndStatus(email, 1).get();
        feedbackEntity.setComment(feedbackDto.getComment());
        feedbackEntity.setRatedStar(feedbackDto.getRatedStar());
        feedbackEntity.setCustomerEntity(userEntity.getCustomerEntity());
        feedbackEntity.setProductEntity(productEntity);
        feedbackEntity.setCreatedAt(LocalDateTime.now());
        feedbackEntity.setCreatedBy(email);
        feedbackEntity.setUpdatedAt(LocalDateTime.now());
        feedbackEntity.setUpdatedBy(email);
        feedbackEntity.setStatus(1);
        feedbackRepository.save(feedbackEntity);
    }

    @Override
    public Long countComments(Long productId) {
        List<FeedbackEntity> feedbackEntities = feedbackRepository.getAllByProductEntity_IdAndStatus(productId, 1);
        Long size = (long) feedbackEntities.size();
        for (FeedbackEntity f : feedbackEntities) {
            if(f.getReplyEntity() != null) {
                size++;
            }
        }
        return size;
    }

    @Override
    public void updateFeedback(FeedbackDto feedbackDto, String email) {
        FeedbackEntity feedbackEntity = feedbackRepository.findById(feedbackDto.getId()).get();
        feedbackEntity.setComment(feedbackDto.getComment());
        feedbackEntity.setRatedStar(feedbackDto.getRatedStar());
        feedbackEntity.setUpdatedAt(LocalDateTime.now());
        feedbackEntity.setUpdatedBy(email);
        feedbackRepository.save(feedbackEntity);
    }

    @Override
    public void deleteFeedback(Long id, String email) {
        FeedbackEntity feedbackEntity = feedbackRepository.findById(id).get();
        feedbackEntity.setUpdatedAt(LocalDateTime.now());
        feedbackEntity.setUpdatedBy(email);
        feedbackEntity.setStatus(0);
        feedbackRepository.save(feedbackEntity);
    }
}
