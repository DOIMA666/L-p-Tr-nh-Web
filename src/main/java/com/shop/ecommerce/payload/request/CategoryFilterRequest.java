package com.shop.ecommerce.payload.request;

import lombok.Data;

@Data
public class CategoryFilterRequest {
    private String name;
    private Integer status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    
    
}
