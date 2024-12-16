package com.shop.ecommerce.payload.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private List<OrderDetailDto> details;
    private BigDecimal totalCost;
    private BigDecimal fullCost;
    private Long userId;
    private String email;
    private String day;
    private String month;
    private String createdDate;
    private String orderStatus;
    private Integer status;
    private Integer detailCount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<OrderDetailDto> getDetails() {
		return details;
	}
	public void setDetails(List<OrderDetailDto> details) {
		this.details = details;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	public BigDecimal getFullCost() {
		return fullCost;
	}
	public void setFullCost(BigDecimal fullCost) {
		this.fullCost = fullCost;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDetailCount() {
		return detailCount;
	}
	public void setDetailCount(Integer detailCount) {
		this.detailCount = detailCount;
	}
    
    
}
