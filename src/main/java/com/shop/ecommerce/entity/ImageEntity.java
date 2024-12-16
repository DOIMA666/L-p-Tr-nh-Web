package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;


@Data
@Entity
@Table(name = "Images")
public class ImageEntity extends AbstractEntity {
    @Basic
    @Column(name = "image_link", nullable = true, length = 255)
    private String imageLink;

    @OneToMany(mappedBy = "imageEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ProductImageEntity> productImageEntities;

    @OneToOne(mappedBy = "avatar")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private UserEntity userEntity;

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public Set<ProductImageEntity> getProductImageEntities() {
		return productImageEntities;
	}

	public void setProductImageEntities(Set<ProductImageEntity> productImageEntities) {
		this.productImageEntities = productImageEntities;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
    
    

}
