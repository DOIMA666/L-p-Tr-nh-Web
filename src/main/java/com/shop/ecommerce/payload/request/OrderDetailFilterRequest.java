package com.shop.ecommerce.payload.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailFilterRequest {
    private Integer quantity;
    private BigDecimal priceOfOne;
    private BigDecimal totalPrice;
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
    
    

}
