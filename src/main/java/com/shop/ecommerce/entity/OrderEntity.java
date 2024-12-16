package com.shop.ecommerce.entity;

import com.shop.ecommerce.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "Orders")
public class OrderEntity extends AbstractEntity{
    @Basic
    @Column(name = "order_time", nullable = true)
    private LocalDateTime orderTime;

    @Basic
    @Column(name = "total_cost", nullable = true)
    private BigDecimal totalCost;

    @Basic
    @Column(name = "shipping_fee", nullable = true)
    private BigDecimal shippingFee;

    @Basic
    @Column(name = "full_cost", nullable = true)
    private BigDecimal fullCost;

    @Basic
    @Column(name = "transaction_code", nullable = true, length = 255)
    private String orderInfo;

    @Basic
    @Column(name = "transaction_status", nullable = true, length = 255)
    private String transactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private OrderStatusEnum orderStatusEnum;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderDetailEntity> orderDetailEntities;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @EqualsAndHashCode.Exclude
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @EqualsAndHashCode.Exclude
    private AddressEntity addressEntity;

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	public BigDecimal getFullCost() {
		return fullCost;
	}

	public void setFullCost(BigDecimal fullCost) {
		this.fullCost = fullCost;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public OrderStatusEnum getOrderStatusEnum() {
		return orderStatusEnum;
	}

	public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
		this.orderStatusEnum = orderStatusEnum;
	}

	public Set<OrderDetailEntity> getOrderDetailEntities() {
		return orderDetailEntities;
	}

	public void setOrderDetailEntities(Set<OrderDetailEntity> orderDetailEntities) {
		this.orderDetailEntities = orderDetailEntities;
	}

	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	public AddressEntity getAddressEntity() {
		return addressEntity;
	}

	public void setAddressEntity(AddressEntity addressEntity) {
		this.addressEntity = addressEntity;
	}
    
    

}
