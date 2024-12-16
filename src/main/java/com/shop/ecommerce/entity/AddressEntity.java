package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Address",
        catalog = "")
@Data
public class AddressEntity extends AbstractEntity {
    @Basic
    @Column(name = "province", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String province;

    @Basic
    @Column(name = "district", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String district;

    @Basic
    @Column(name = "ward", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String ward;

    @Basic
    @Column(name = "details", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String details;

    @OneToMany(mappedBy = "addressEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderEntity> orderEntities = new HashSet<>();

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Set<OrderEntity> getOrderEntities() {
		return orderEntities;
	}

	public void setOrderEntities(Set<OrderEntity> orderEntities) {
		this.orderEntities = orderEntities;
	}

}
