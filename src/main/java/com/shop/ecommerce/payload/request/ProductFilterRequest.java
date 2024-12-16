package com.shop.ecommerce.payload.request;

import lombok.Data;

@Data
public class ProductFilterRequest {
    private String productName;
    private Long saleStartPrice;
    private Long saleEndPrice;
    private Long startPrice;
    private Long endPrice;
    private Integer status;
    private Long categoryId;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getSaleStartPrice() {
		return saleStartPrice;
	}
	public void setSaleStartPrice(Long saleStartPrice) {
		this.saleStartPrice = saleStartPrice;
	}
	public Long getSaleEndPrice() {
		return saleEndPrice;
	}
	public void setSaleEndPrice(Long saleEndPrice) {
		this.saleEndPrice = saleEndPrice;
	}
	public Long getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(Long startPrice) {
		this.startPrice = startPrice;
	}
	public Long getEndPrice() {
		return endPrice;
	}
	public void setEndPrice(Long endPrice) {
		this.endPrice = endPrice;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    
    
}
