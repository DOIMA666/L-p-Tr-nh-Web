package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "Products")
public class ProductEntity extends AbstractEntity{
    @Basic
    @Column(length = 255,name = "product_name", nullable = true,  columnDefinition = "nvarchar(255)")
    private String productName;

    @Basic
    @Column(name = "thumbnail", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String thumbnail;

    @Basic
    @Column(name = "description", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String description;

    @Basic
    @Column(name = "amount", nullable = true)
    private Integer amount;

    @Basic
    @Column(name = "price", nullable = true)
    private BigDecimal price;

    @Basic
    @Column(name = "sale_price", nullable = true)
    private BigDecimal salePrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FeedbackEntity> feedbackEntities;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderDetailEntity> orderDetailEntities;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ProductImageEntity> productImageEntities;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public CategoryEntity getCategoryEntity() {
		return categoryEntity;
	}

	public void setCategoryEntity(CategoryEntity categoryEntity) {
		this.categoryEntity = categoryEntity;
	}

	public Set<FeedbackEntity> getFeedbackEntities() {
		return feedbackEntities;
	}

	public void setFeedbackEntities(Set<FeedbackEntity> feedbackEntities) {
		this.feedbackEntities = feedbackEntities;
	}

	public Set<OrderDetailEntity> getOrderDetailEntities() {
		return orderDetailEntities;
	}

	public void setOrderDetailEntities(Set<OrderDetailEntity> orderDetailEntities) {
		this.orderDetailEntities = orderDetailEntities;
	}

	public Set<ProductImageEntity> getProductImageEntities() {
		return productImageEntities;
	}

	public void setProductImageEntities(Set<ProductImageEntity> productImageEntities) {
		this.productImageEntities = productImageEntities;
	}
    
    


}
