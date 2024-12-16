package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Reply",
        catalog = "")
@Data
public class ReplyEntity extends AbstractEntity {

    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    private FeedbackEntity feedbackEntity;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public FeedbackEntity getFeedbackEntity() {
		return feedbackEntity;
	}

	public void setFeedbackEntity(FeedbackEntity feedbackEntity) {
		this.feedbackEntity = feedbackEntity;
	}
    
    
}
