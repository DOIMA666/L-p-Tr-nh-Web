package com.shop.ecommerce.payload.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderFilterRequest {
    private BigDecimal fullCost;
    private String day;
    private String month;
    private String createdDate;
    private Integer status;
	public BigDecimal getFullCost() {
		return fullCost;
	}
	public void setFullCost(BigDecimal fullCost) {
		this.fullCost = fullCost;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    
    
}
