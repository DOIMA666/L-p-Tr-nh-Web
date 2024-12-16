package com.shop.ecommerce.payload.dto;

import lombok.Data;

@Data
public class FeedbackDto {
    private Long id;
    private Integer ratedStar;
    private String comment;
    private ReplyDto replyDto;
    private Long customerId;
    private Long userId;
    private String email;
    private Long productId;
    private String productName;
    private String avatar;
    private String fullName;
    private String createdDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRatedStar() {
		return ratedStar;
	}
	public void setRatedStar(Integer ratedStar) {
		this.ratedStar = ratedStar;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ReplyDto getReplyDto() {
		return replyDto;
	}
	public void setReplyDto(ReplyDto replyDto) {
		this.replyDto = replyDto;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
    
    
}
