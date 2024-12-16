package com.shop.ecommerce.payload.wrapper;

import com.shop.ecommerce.entity.CartDetailEntity;
import lombok.Data;

import java.util.List;
@Data
public class CartDetailWrapper {
    private List<CartDetailEntity> cartDetailEntities;

	public List<CartDetailEntity> getCartDetailEntities() {
		return cartDetailEntities;
	}

	public void setCartDetailEntities(List<CartDetailEntity> cartDetailEntities) {
		this.cartDetailEntities = cartDetailEntities;
	}
    
    
}
