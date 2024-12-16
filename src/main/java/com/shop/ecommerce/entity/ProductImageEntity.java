package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "ProductImages")
public class ProductImageEntity extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Exclude
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @EqualsAndHashCode.Exclude
    private ImageEntity imageEntity;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;

	public ProductEntity getProductEntity() {
		return productEntity;
	}

	public void setProductEntity(ProductEntity productEntity) {
		this.productEntity = productEntity;
	}

	public ImageEntity getImageEntity() {
		return imageEntity;
	}

	public void setImageEntity(ImageEntity imageEntity) {
		this.imageEntity = imageEntity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    
}
