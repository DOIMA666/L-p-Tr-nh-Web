package com.shop.ecommerce.payload.request;

import lombok.Data;

@Data
public class UserFilterRequest {
    private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    
}
