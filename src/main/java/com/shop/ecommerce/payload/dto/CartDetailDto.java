package com.shop.ecommerce.payload.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartDetailDto {
    private Long id;
    private Integer quantity;
    private BigDecimal priceOfOne;
    private BigDecimal totalPrice;
    private Long cartId;
    private String createdDate;
    private String thumbnail;
    private String productName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPriceOfOne() {
		return priceOfOne;
	}
	public void setPriceOfOne(BigDecimal priceOfOne) {
		this.priceOfOne = priceOfOne;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
    
    
}
