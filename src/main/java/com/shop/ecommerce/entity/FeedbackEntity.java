package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Feedback",
        catalog = "")
@Data
public class FeedbackEntity extends AbstractEntity {

    @Basic
    @Column(name = "rated_star", nullable = true)
    private Integer ratedStar;

    @Basic
    @Column(name = "comment", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @EqualsAndHashCode.Exclude
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Exclude
    private ProductEntity productEntity;

    @OneToOne(mappedBy = "feedbackEntity")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private ReplyEntity replyEntity;

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

	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	public ProductEntity getProductEntity() {
		return productEntity;
	}

	public void setProductEntity(ProductEntity productEntity) {
		this.productEntity = productEntity;
	}

	public ReplyEntity getReplyEntity() {
		return replyEntity;
	}

	public void setReplyEntity(ReplyEntity replyEntity) {
		this.replyEntity = replyEntity;
	}
    
    
}
