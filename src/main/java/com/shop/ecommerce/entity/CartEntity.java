package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "Cart")
public class CartEntity extends AbstractEntity{

    @Basic
    @Column(name = "total_cost", nullable = true)
    private BigDecimal totalCost;

    @OneToOne(mappedBy = "cartEntity")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private CustomerEntity customerEntity;

    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CartDetailEntity> cartDetailEntities;

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	public Set<CartDetailEntity> getCartDetailEntities() {
		return cartDetailEntities;
	}

	public void setCartDetailEntities(Set<CartDetailEntity> cartDetailEntities) {
		this.cartDetailEntities = cartDetailEntities;
	}

    
}
