package com.shop.ecommerce.payload.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String categoryName;
    private String createdDate;
    private Integer countProducts;
    private Integer status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCountProducts() {
		return countProducts;
	}
	public void setCountProducts(Integer countProducts) {
		this.countProducts = countProducts;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    
    
}
