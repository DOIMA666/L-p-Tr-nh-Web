package com.shop.ecommerce.payload.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductImageDto {
    private Long imageId;
    private String imageLink;
    private String description;
    private Long productId;
	public Long getImageId() {
		return imageId;
	}
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
    
    
}
