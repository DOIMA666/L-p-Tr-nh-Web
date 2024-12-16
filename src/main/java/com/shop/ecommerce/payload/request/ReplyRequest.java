package com.shop.ecommerce.payload.request;

import lombok.Data;

@Data
public class ReplyRequest {
    private Long id;
    private Long feedbackId;
    private Long productId;
    private Long userId;
    private String content;
    private String createdDate;
    private String email;
    private String avatarUser;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatarUser() {
		return avatarUser;
	}
	public void setAvatarUser(String avatarUser) {
		this.avatarUser = avatarUser;
	}
    
    
}
